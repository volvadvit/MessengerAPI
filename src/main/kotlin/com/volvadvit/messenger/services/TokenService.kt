package com.volvadvit.messenger.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.volvadvit.messenger.exceptions.InvalidTokenException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class TokenService {

    //TODO change RedisCommands<String, String> to RedisTemplate

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

    private lateinit var algorithm : Algorithm
    private lateinit var verifier : JWTVerifier
    private lateinit var syncCommands: RedisCommands<String, String>
    private val BEARER = "Bearer"
    private val bearerLength = "$BEARER ".length

    fun generateJwtToken(username: String, roles: List<String?>?): Map<String, String> {
        syncCommands = setUpRedis()

        val accessToken = JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenExp.toLong()))
            .withIssuedAt(Timestamp.from(Instant.now()))
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject(username)
            .withIssuedAt(Timestamp.from(Instant.now()))
            .sign(algorithm)

        syncCommands.set("token:$username", refreshToken)
        syncCommands.expire("token:$username", refreshTokenExp.toLong())

        logger.info("Create token pair access: {}, refresh: {}", accessToken, refreshToken)

        return mapOf(
            "access_token" to accessToken,
            "token_type" to BEARER,
            "expires_in" to accessTokenExp,
            "refresh_token" to refreshToken
        )
    }

    fun verifiedAccessToken(authorizationHeader: String) : UsernamePasswordAuthenticationToken {
        syncCommands = setUpRedis()

        val token = authorizationHeader.substring(bearerLength)
        val decodedJWT = verifier.verify(token)

        val username = decodedJWT.subject

        if (!username.isNullOrEmpty()) {
            val authenticationToken = UsernamePasswordAuthenticationToken(username, null, emptyList())

            logger.info("User $username is verified.")

            return authenticationToken
        } else {
            throw InvalidTokenException("Invalid or empty user credentials. Cannot verify token")
        }
    }

    fun refreshTokenPair(authorizationHeader: String, userService: UserService): Map<String, String> {
        syncCommands = setUpRedis()

        val username = getUsernameFromToken(authorizationHeader)
        val tokenFromRequest = authorizationHeader.substring(bearerLength)
        val tokenFromRedis = syncCommands.get("token:$username")

        return if (!tokenFromRedis.isNullOrEmpty() && tokenFromRedis == tokenFromRequest) {

            logger.info("Found existing refresh token: {}", tokenFromRedis)

            val user = userService.getByUsername(username)
                ?: throw UsernameUnavailableException("User for provided token not found")
            val roles: List<String> = user.roles.map{ role ->  role.name}
                ?: throw InvalidTokenException("Cannot get user authorities")
            generateJwtToken(username, roles)
        } else {
            throw InvalidTokenException("Token from redis is invalid. Your token is '$tokenFromRequest'; redis token is '$tokenFromRedis'")
        }
    }

    fun deleteToken(username: String) {
        syncCommands = setUpRedis()
        val redisKey = "token:$username"
        syncCommands.expire(redisKey, 1.toLong())
    }

    private fun getUsernameFromToken(authorizationHeader: String?): String {
        return if (authorizationHeader != null && authorizationHeader.startsWith("$BEARER ")) {
            val token = authorizationHeader.substring(bearerLength)
            val decodedJWT = verifier.verify(token)
            decodedJWT.subject
        } else {
            throw InvalidTokenException("Bad authorization header. $authorizationHeader")
        }
    }

    private fun setUpRedis(): RedisCommands<String, String> {
        if (this::syncCommands.isInitialized) {
            return syncCommands
        }

        algorithm = Algorithm.HMAC256(jwtSecret.toByteArray())
        verifier = JWT.require(algorithm).build()

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