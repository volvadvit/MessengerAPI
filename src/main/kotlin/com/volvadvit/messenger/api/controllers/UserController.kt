package com.volvadvit.messenger.api.controllers

import com.volvadvit.messenger.api.components.UserAssembler
import com.volvadvit.messenger.api.helpers.objects.UserListVO
import com.volvadvit.messenger.api.helpers.objects.UserVO
import com.volvadvit.messenger.api.models.User
import com.volvadvit.messenger.api.repositaries.UserRepository
import com.volvadvit.messenger.api.services.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UserController(val userService: UserServiceImpl,
                     val userAssembler: UserAssembler, val userRepository: UserRepository) {

    @PostMapping
    @RequestMapping("/registrations")
    fun create(@Validated @RequestBody userDetails: User?) : ResponseEntity<UserVO> {
        val user = userDetails?.let { userService.attemptRegistration(it) }
        return ResponseEntity.ok(userAssembler.toUserVO(user!!))
    }

    @GetMapping
    @RequestMapping("/{user_id}")
    fun show(@PathVariable("user_id") userId: Long) : ResponseEntity<UserVO> {
        val user = userService.retrieveUserData(userId)
        return  ResponseEntity.ok(userAssembler.toUserVO(user!!))
    }

    @GetMapping
    @RequestMapping("/details")
    fun echoDetails(request: HttpServletRequest) : ResponseEntity<UserVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    @GetMapping
    fun index(request: HttpServletRequest) : ResponseEntity<UserListVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        val users = userService.listUsers(user)
        return ResponseEntity.ok(userAssembler.toUserListVO(users))
    }

    @PutMapping
    fun update(@RequestBody updateDetails: User, request: HttpServletRequest) : ResponseEntity<UserVO> {
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        userService.updateUserStatus(currentUser!!, updateDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }
}