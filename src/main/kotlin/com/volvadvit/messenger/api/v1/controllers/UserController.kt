package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.assemblers.UserAssembler
import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.api.v1.models.UserListVO
import com.volvadvit.messenger.api.v1.models.UserVO
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.TokenService
import com.volvadvit.messenger.services.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
class UserController(
    val userService: UserService,
    val userAssembler: UserAssembler,
    val tokenService: TokenService
) {

    @ApiOperation("Method for user registration",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        response = UserVO::class)
    @RequestMapping("/save", method = [RequestMethod.POST])
    fun create(@Valid @RequestBody userDetails: User?) : ResponseEntity<ResponseMapper> {
        val user = userService.attemptRegistration(userDetails)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "User created",
                userAssembler.toUserVO(user),
                Date.from(Instant.now())
            )
        )
    }

    @ApiOperation(
        value = "Get user details by specified user_id",
        response = UserVO::class)
    @RequestMapping("/{user_id}", method = [RequestMethod.GET])
    fun show(@PathVariable("user_id") userId: Long) : ResponseEntity<ResponseMapper> {
        val user = userService.getById(userId)
        return  ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Return user for provided id",
                userAssembler.toUserVO(user),
                Date.from(Instant.now())
            ))
    }

    @ApiOperation("Get current user details", response = UserVO::class)
    @RequestMapping("/details", method = [RequestMethod.GET])
    fun userDetails(request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val user = userService.getByUsername(request.userPrincipal.name)
        return ResponseEntity.ok().body(ResponseMapper(
            HttpStatus.OK.value(),
            "Return current user details",
            userAssembler.toUserVO(user),
            Date.from(Instant.now())
        ))
    }

    @ApiOperation("Return list of all users except of current user", response = UserListVO::class)
    @GetMapping
    fun getAllUsers(request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val user = userService.getByUsername(request.userPrincipal.name)
        val users = userService.listUsersExceptOf(user)
        return ResponseEntity.ok().body(ResponseMapper(
            HttpStatus.OK.value(),
            "Return other users",
            userAssembler.toUserListVO(users),
            Date.from(Instant.now())
        ))
    }

    @GetMapping("/token/refresh")
    @ApiOperation("Method to generate new pair of access/refresh tokens for current user. Required REFRESH token in Authorization header")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse?): Map<String, String>? {
        return tokenService.refreshTokenPair(request.getHeader(HttpHeaders.AUTHORIZATION), userService)
    }

    @ApiOperation("Update current user info with provided UpdatedUserDetails model", response = UserListVO::class)
    @PutMapping
    fun update(@RequestBody updateData: User, request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val currentUser = userService.getByUsername(request.userPrincipal.name)
        userService.updateUser(currentUser, updateData)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Successfully update user",
                userAssembler.toUserVO(currentUser),
                Date.from(Instant.now())
            ))
    }

    @ApiOperation("Delete current user", response = Boolean::class)
    @DeleteMapping
    fun deleteUser(request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val user = userService.getByUsername(request.userPrincipal.name)
        userService.deleteUser(user)
        return ResponseEntity.ok().body(
            ResponseMapper(
            HttpStatus.OK.value(),
            "Successfully delete user",
            true,
                Date.from(Instant.now())
        ))
    }
}