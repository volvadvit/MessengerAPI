package com.volvadvit.messenger.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.exceptions.FailedLoginAttemptException
import com.volvadvit.messenger.exceptions.UserNotExists
import com.volvadvit.messenger.models.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.sql.Timestamp
import java.time.Instant

@Service
class VKAuthService(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(VKAuthService::class.java)
    private val FAILED_LOGIN_MESSAGE = "Failed to login through VK authentication"
    private val BEARER = "Bearer "

    @Value("\${vk.auth.app.id}")
    private lateinit var clientId: String
    @Value("\${vk.auth.app.secret}")
    private lateinit var clientSecret: String
    @Value("\${vk.auth.redirect.url}")
    private lateinit var redirectUrl: String

    fun loginOAuthUser(code: String) : Map<String, String> {
        val userDetails = getVKUserDetails(code)
        return try {
            val user = userService.getByEmail(userDetails.email)
            logger.info("Login with existing VK user: ${user.username}, ${user.email}")
            authorizeUser(user)
        } catch (e: UserNotExists) {
            logger.info("Register new VK user: ${userDetails.firstName}, ${userDetails.email}")
            val user = userService.saveOAuthUser(convertFrom(userDetails))
            authorizeUser(user)
        }
    }

    private fun authorizeUser(user: User): Map<String, String> {
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user.username, null, user.roles)
        return tokenService.generateJwtToken(
            user.username,
            user.roles.map { role -> role.name }
        )
    }

    private fun getVKUserDetails(code: String) : VKUserDTO {
        val tokenUrl = "https://oauth.vk.com/access_token?client_id=$clientId&client_secret=$clientSecret&redirect_uri=$redirectUrl&code=$code"
        val tokenResponse: ResponseEntity<String> = restTemplate.getForEntity(tokenUrl, String::class.java)
        if (tokenResponse.statusCode != HttpStatus.OK) {
            val error = objectMapper.readValue(tokenResponse.body, VKErrorResponseModel::class.java)
            logger.error("Error response code from VK: ${tokenResponse.statusCode} \n ${error.error} \n ${error.error_description}")
            throw FailedLoginAttemptException("$FAILED_LOGIN_MESSAGE. ${error.error_description}")
        }
        try {
            val tokenResult: VKTokenResponseModel = objectMapper.readValue(tokenResponse.body, VKTokenResponseModel::class.java)
            val userUrl = "https://api.vk.com/method/users.get?user_id=${tokenResult.user_id}&v=5.131&fields=photo_200"
            val headers = HttpHeaders()
            headers[HttpHeaders.AUTHORIZATION] = listOf(BEARER + tokenResult.access_token)
            val requestEntity = HttpEntity<String>(headers)
            val userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, requestEntity, String::class.java)
            if (userResponse.statusCode != HttpStatus.OK) {
                val error = objectMapper.readValue(tokenResponse.body, VKErrorResponseModel::class.java)
                logger.error("Error response user details from VK: ${tokenResponse.statusCode} \n ${error.error} \n ${error.error_description}")
                throw FailedLoginAttemptException("$FAILED_LOGIN_MESSAGE. ${error.error_description}")
            }
            val userResult = objectMapper.readValue(userResponse.body, VKUserResponseModel::class.java)
            return VKUserDTO(
                userResult.first_name,
                userResult.last_name,
                userResult.photo_200,
                tokenResult.email
            )
        } catch (e: Exception) {
            logger.error("Error while parsing response from VK: ${tokenResponse.body}")
            throw FailedLoginAttemptException(FAILED_LOGIN_MESSAGE)
        }
    }
}

private fun convertFrom(user: VKUserDTO) : User {
    val newUser = User()
    return newUser.apply {
        username = user.firstName + " " + user.lastName
        email = user.email
        photoUrl = user.photoUrl
        createdAt = Timestamp.from(Instant.now())
        lastActive = Timestamp.from(Instant.now())
    }
}

data class VKUserResponseModel(
    val id : Long,
    val first_name: String,
    val last_name: String,
    val can_access_closed : Boolean,
    val is_closed : Boolean,
    val photo_200: String
)
data class VKUserDTO(
    val firstName: String,
    val lastName: String,
    val photoUrl: String,
    val email: String
)

data class VKTokenResponseModel(
    val access_token: String,
    val expires_in: Int,
    val user_id: Long,
    val email: String
)

data class VKErrorResponseModel(
    val error: String,
    val error_description: String
)