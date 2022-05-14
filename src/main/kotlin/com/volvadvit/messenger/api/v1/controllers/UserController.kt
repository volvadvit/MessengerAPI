package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.assemblers.UserAssembler
import com.volvadvit.messenger.api.v1.models.*
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.services.TokenService
import com.volvadvit.messenger.services.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.sql.Timestamp
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/v1/users")
class UserController (
    val userService: UserService,
    val userAssembler: UserAssembler,
    val tokenService: TokenService
) {

    @ApiOperation("Method for user registration",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        response = UserVO::class)
    @RequestMapping("/save", method = [RequestMethod.POST])
    fun create(@RequestBody userDetails: User?) : ResponseEntity<ResponseMapper> {
        val user = userService.attemptRegistration(userDetails)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "User created",
                userAssembler.toUserVO(user),
                Timestamp.from(Instant.now())
            )
        )
    }

    @ApiOperation("Get user details by specified user_id", response = UserVO::class)
    @RequestMapping("/{user_id}", method = [RequestMethod.GET])
    fun show(@PathVariable("user_id") userId: Long) : ResponseEntity<ResponseMapper> {
        val user = userService.getById(userId)
        return  ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Return user for provided id",
                userAssembler.toUserVO(user),
                Timestamp.from(Instant.now())
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
            Timestamp.from(Instant.now())
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
            Timestamp.from(Instant.now())
        ))
    }

    @GetMapping("/token/refresh")
    @ApiOperation("Method to generate new pair of access/refresh tokens for current user. Required REFRESH token in Authorization header")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest): Map<String, String>? {
        return tokenService.refreshTokenPair(request.getHeader(HttpHeaders.AUTHORIZATION), userService)
    }

    @ApiOperation("Update current user info", response = UserListVO::class)
    @PutMapping
    fun update(@RequestBody updateData: User?, request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val currentUser = userService.getByUsername(request.userPrincipal.name)
        userService.updateUser(currentUser, updateData)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Successfully update user",
                userAssembler.toUserVO(currentUser),
                Timestamp.from(Instant.now())
            ))
    }

    @ApiOperation("Delete current user and token", response = Boolean::class)
    @DeleteMapping
    fun deleteUser(request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val user = userService.getByUsername(request.userPrincipal.name)
        userService.deleteUser(user)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Successfully delete user",
                true,
                Timestamp.from(Instant.now())
            ))
    }

    @ApiOperation("Update user password. Required new password in Base64 encode")
    @RequestMapping("/pwd/update", method = [RequestMethod.PUT])
    fun updatePassword(@RequestParam("password") password: String?,
                       request: HttpServletRequest) : ResponseEntity<ResponseMapper>
    {
        val user = userService.getByUsername(request.userPrincipal.name)
        userService.updateUserPassword(user, password)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Password updated",
                null,
                Timestamp.from(Instant.now()))
        )
    }

    @ApiOperation("Method for adding friend to current user. Return list of friends for user",
        response = ShortUserListVO::class)
    @RequestMapping("/friends/add", method = [RequestMethod.POST])
    fun addFriend(@RequestParam friendId: Long?, request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val current = userService.getByUsername(request.userPrincipal.name)
        val updated = userService.addFriend(current, friendId)
        return ResponseEntity.ok().body(ResponseMapper(
            HttpStatus.OK.value(),
            "List of friends for ${updated.username}",
            updated.friendsId.map { id -> userAssembler.toShortUserVO(userService.getById(id)) },
            Timestamp.from(Instant.now())
        ))
    }

    @ApiOperation("Get friends of current user", response = ShortUserListVO::class)
    @RequestMapping("/friends", method = [RequestMethod.GET])
    fun getFriends(request: HttpServletRequest) : ResponseEntity<ResponseMapper> {
        val current = userService.getByUsername(request.userPrincipal.name)
        val friends: List<ShortUserVO> = current.friendsId.map { id -> userAssembler.toShortUserVO(userService.getById(id)) }
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "List of friends for ${current.username}",
                friends,
                Timestamp.from(Instant.now())
            )
        )
    }
}