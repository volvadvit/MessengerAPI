package com.volvadvit.messenger.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.services.TokenService
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter (private val authManager: AuthenticationManager)
    : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): Authentication? {
        val username = request.getParameter("username")
        val password = request.getParameter("password")
        logger.info("Username is: $username")
        logger.info("Password is: $password")
        return authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                password
            )
        )
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?, authResult: Authentication?
    ) {
        if (authResult != null && response != null) {
            val user = authResult.principal as User
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            ObjectMapper().writeValue(
                response.outputStream,
                TokenService.generateJwtToken(
                    user.username,
                    user.authorities.stream()
                        .map { obj: GrantedAuthority -> obj.authority }
                        .collect(Collectors.toList())
                )
            )
        }
    }
}