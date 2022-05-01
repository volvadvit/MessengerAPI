package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.models.UserListVO
import com.volvadvit.messenger.api.v1.models.UserVO
import com.volvadvit.messenger.api.v1.assemblers.UserAssembler
import com.volvadvit.messenger.api.v1.models.UpdatedUserDetails
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.impl.UserServiceImpl
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/users")
class UserController(
    val userService: UserServiceImpl,
    val userAssembler: UserAssembler,
    val userRepository: UserRepository
) {

    @RequestMapping("/registrations", method = [RequestMethod.POST])
    fun create(@Validated @RequestBody userDetails: User?) : ResponseEntity<UserVO> {
        val user = userDetails?.let { userService.attemptRegistration(it) }
        return ResponseEntity.ok(userAssembler.toUserVO(user!!))
    }

    @ApiOperation(
        value = "Get user details by specified user_id",
        response = UserVO::class)
    @RequestMapping("/{user_id}", method = [RequestMethod.GET])
    fun show(@PathVariable("user_id") userId: Long) : ResponseEntity<UserVO> {
        val user = userService.retrieveUserData(userId)
        return  ResponseEntity.ok(userAssembler.toUserVO(user!!))
    }

    @RequestMapping("/details", method = [RequestMethod.GET], name = "Get currently logger user details")
    fun echoDetails(request: HttpServletRequest) : ResponseEntity<UserVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    @ApiOperation(
        value = "Return list of all users except of currently logged user", response = UserListVO::class)
    @GetMapping
    fun index(request: HttpServletRequest) : ResponseEntity<UserListVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        val users = userService.listUsers(user)
        return ResponseEntity.ok(userAssembler.toUserListVO(users))
    }

    @ApiOperation(value = "Update currently logged user info with provided user details", response = UserListVO::class)
    @PutMapping
    fun update(@RequestBody updateDetails: UpdatedUserDetails, request: HttpServletRequest) : ResponseEntity<UserVO> {
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        userService.updateUserStatus(currentUser!!, updateDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }
}