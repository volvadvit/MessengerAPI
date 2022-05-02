package com.volvadvit.messenger.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.services.TokenService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain)
    {
        if ("/login" == request.servletPath || "/user/token/refresh" == request.servletPath) {
            filterChain.doFilter(request, response)
        } else {
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    TokenService.verifiedAccessToken(authorizationHeader)
                    logger.info("Token is verified")
                    filterChain.doFilter(request, response)
                } catch (e: Exception) {
                    logger.error("Error in authorization filter: ${e.message}")

                    response.setHeader("error", e.message)
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.contentType = MediaType.APPLICATION_JSON_VALUE

                    ObjectMapper().writeValue(
                        response.outputStream,
                        ResponseMapper(HttpStatus.UNAUTHORIZED.value(), "error logging", e.message!!)
                    )
                }
            } else {
                filterChain.doFilter(request, response)
            }
        }
    }
}