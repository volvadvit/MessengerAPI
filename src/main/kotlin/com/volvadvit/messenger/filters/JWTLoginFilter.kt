package com.volvadvit.messenger.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.models.Role
import com.volvadvit.messenger.security.AccountCredentials
import com.volvadvit.messenger.services.TokenAuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(url: String, authManager: AuthenticationManager)
    : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {

    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(p0: HttpServletRequest?, p1: HttpServletResponse?): Authentication {
        val credentials = ObjectMapper().readValue(p0?.inputStream, AccountCredentials::class.java)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentials.username,
                credentials.password,
                credentials.roles
            )
        )
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        if (response != null && authResult != null) {
            TokenAuthenticationService.addAuthentication(response, authResult.name, authResult.authorities)
        } else {
            throw ServletException()
        }
    }
}