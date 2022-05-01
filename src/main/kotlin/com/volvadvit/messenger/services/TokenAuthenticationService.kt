package com.volvadvit.messenger.services

import com.volvadvit.messenger.models.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashSet

private const val BAD_TOKEN_ERROR_MESSAGE = "Authentication denied. Bad access token"

internal object TokenAuthenticationService {
    private val TOKEN_EXPIRY: Long = 864000000
    private val SECRET = "$78gr43g7g8feb8we"
    private val TOKEN_PREFIX = "Bearer"
    private val AUTHORIZATION_HEAD_KEY = "Authorization"

    fun addAuthentication(res: HttpServletResponse, username: String, authorities: Collection<GrantedAuthority>) {
        val subject = generateJWTSubject(username, authorities)
        val JWT = Jwts.builder()
            .setSubject(subject.toString())
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_EXPIRY))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact()
        res.addHeader(AUTHORIZATION_HEAD_KEY, "$TOKEN_PREFIX $JWT")
    }

    fun getAuthentication(request: HttpServletRequest): Authentication {
        val token = request.getHeader(AUTHORIZATION_HEAD_KEY)
        if (token != null) {
            val subject = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX,  ""))
                .body.subject
            if (subject.isNullOrBlank()) {
                throw RuntimeException(BAD_TOKEN_ERROR_MESSAGE)
            }
            val username = parseJwtForUsername(subject)
            val roles = parseJwtForAuthorities(subject)
            return UsernamePasswordAuthenticationToken(username, null, roles)
        }
        throw RuntimeException(BAD_TOKEN_ERROR_MESSAGE)
    }

    private fun parseJwtForUsername(subject: String): String {
        val username = subject.split(":")[0]
        return username.ifEmpty { throw RuntimeException(BAD_TOKEN_ERROR_MESSAGE) }
    }

    private fun parseJwtForAuthorities(subject: String): Collection<Role> {
        val roles = HashSet<Role>()
        val subjects = subject.split(":")
        if (subjects.size < 2) {
            throw RuntimeException(BAD_TOKEN_ERROR_MESSAGE)
        }
        for (i in 1..subjects.size) {
            roles.add(Role.valueOf(subjects[i]))
        }
        return roles
    }

    private fun generateJWTSubject(
        username: String,
        authorities: Collection<GrantedAuthority>
    ): StringBuilder {
        val subject = StringBuilder(username)
        authorities.forEach { role ->
            subject.append(":")
            subject.append(role.authority)
        }
        return subject
    }
}