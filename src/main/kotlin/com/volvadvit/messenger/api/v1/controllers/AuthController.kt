package com.volvadvit.messenger.api.v1.controllers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.services.VKAuthService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp
import java.time.Instant

@RestController
@RequestMapping("/v1/auth")
class AuthController(private val vkAuthService: VKAuthService) {

    @ApiOperation(
        "Method for login/register user with VK authentication code",
        response = Map::class)
    @RequestMapping("/vk/login", method = [RequestMethod.POST])
    fun authenticateVKUser(@RequestParam code: String) : ResponseEntity<ResponseMapper> {
        val tokens = vkAuthService.loginOAuthUser(code)
        return ResponseEntity.ok().body(
            ResponseMapper(
                HttpStatus.OK.value(),
                "Access & refresh tokens",
                tokens,
                Timestamp.from(Instant.now())
            )
        )
    }

    @ApiOperation("Error handling from VK")
    @RequestMapping("/vk/callback/", method = [RequestMethod.GET])
    fun errorCallbackVK(@RequestParam("error") error: String,
                        @RequestParam("error_description") description: String)
    {
        //TODO
    }
}