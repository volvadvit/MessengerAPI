package com.volvadvit.messenger.filters

import com.volvadvit.messenger.services.TokenAuthenticationService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JWTAuthenticationFilter : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        try {
            val authentication = TokenAuthenticationService.getAuthentication(p0 as HttpServletRequest)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e : RuntimeException) {
            System.err.println("Error due attempting authentication. " + e.message)
        }
        p2?.doFilter(p0, p1)
    }
}