package com.volvadvit.messenger.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.volvadvit.messenger.exceptions.InvalidTokenException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import com.volvadvit.messenger.models.Role
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.Instant
import java.util.*

object TokenService {

    private val logger = LoggerFactory.getLogger(TokenService::class.java)

    @Value("\${redis.host}")
    private lateinit var redisHost : String
    @Value("\${redis.port}")
    private lateinit var redisPort : String
    @Value("\${redis.password}")
    private lateinit var redisPassword : String
    @Value("\${redis.database}")
    private lateinit var redisDatabase : String

    @Value("\${token.secret}")
    private lateinit var jwtSecret : String
    @Value("\${token.access.expr.ms}")
    private lateinit var accessTokenExp : String
    @Value("\${token.refresh.expr.sec}")
    private lateinit var refreshTokenExp : String

    private val algorithm = Algorithm.HMAC256(jwtSecret.toByteArray())
    private val verifier = JWT.require(algorithm).build()
    private const val bearerLength = "Bearer ".length
    private var syncCommands: RedisCommands<String, String>

    init {
        syncCommands = setUpRedis()
    }

    fun generateJwtToken(username: String, roles: List<String?>?): Map<String, String> {
        val accessToken = JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenExp.toLong()))
            .withIssuedAt(Date.from(Instant.now()))
            .withClaim("roles", roles)
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject(username)
            .withIssuedAt(Date.from(Instant.now()))
            .sign(algorithm)

        syncCommands.set("token:$username", refreshToken)
        syncCommands.expire("token:$username", refreshTokenExp.toLong())

        logger.info("Create token pair access: {}, refresh: {}", accessToken, refreshToken)

        return mapOf(
            "access_token" to accessToken,
            "token_type" to "bearer",
            "expires_in" to accessTokenExp,
            "refresh_token" to refreshToken
        )
    }

    fun verifiedAccessToken(authorizationHeader: String) {
        val token = authorizationHeader.substring(bearerLength)
        val decodedJWT = verifier.verify(token)

        val username = decodedJWT.subject
        val roles: List<Role> = decodedJWT.getClaim("roles").asList(Role::class.java)

        if (!username.isNullOrEmpty() || roles.isNotEmpty()) {
            val authenticationToken = UsernamePasswordAuthenticationToken(username, null, roles)
            logger.info("User $username is verified.")
            SecurityContextHolder.getContext().authentication = authenticationToken
        } else {
            throw InvalidTokenException("Invalid or empty user credentials. Cannot verify token")
        }
    }

    fun updateAccessToken(authorizationHeader: String, userService: UserService): Map<String, String> {
        val username = getUsernameFromToken(authorizationHeader)

        val tokenFromRequest = authorizationHeader.substring(bearerLength)
        val tokenFromRedis = syncCommands.get("token:$username")

        return if (!tokenFromRedis.isNullOrEmpty() && tokenFromRedis == tokenFromRequest) {

            logger.info("found existing refresh token: {}", tokenFromRedis)

            val user = userService.retrieveUserData(username)
                ?: throw UsernameUnavailableException("User for provided token not found")
            val roles: List<String> = user.roles?.map{ role ->  role.name}
                ?: throw InvalidTokenException("Cannot get user authorities")
            generateJwtToken(username, roles)
        } else {
            throw InvalidTokenException("Token from redis is invalid. $authorizationHeader")
        }
    }

    fun getUsernameFromToken(authorizationHeader: String?): String {
        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(bearerLength)
            val decodedJWT = verifier.verify(token)
            decodedJWT.subject
        } else {
            throw InvalidTokenException("Bad authorization header. $authorizationHeader")
        }
    }

    private fun setUpRedis(): RedisCommands<String, String> {
        val localhost = RedisURI.Builder
            .redis(redisHost, redisPort.toInt()).withPassword(redisPassword.toCharArray())
            .withDatabase(redisDatabase.toInt()).build()
        val redisClient = RedisClient.create(localhost)
        try {
            val connection = redisClient.connect()
            return connection.sync()
        } catch (ex: Exception) {
            logger.error(ex.message)
            ex.printStackTrace()
        }
        logger.error(
            "Couldn't connect to Redis, host: {}, port: {}, pwd: {}",
            redisHost,
            redisPort,
            redisPassword
        )
        throw RuntimeException("Couldn't connect to Redis")
    }
}