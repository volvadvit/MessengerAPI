package com.volvadvit.messenger.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.constants.Role
import com.volvadvit.messenger.filter.AuthenticationFilter
import com.volvadvit.messenger.filter.AuthorizationFilter
import com.volvadvit.messenger.services.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val tokenService: TokenService,
    private val objectMapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val authenticationFilter = AuthenticationFilter(authenticationManagerBean()!!, tokenService, objectMapper)
        authenticationFilter.setFilterProcessesUrl("/v1/login")

        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/v1/", "/",
                "/v1/login",
                "v1/auth/vk/login",
                "/v1/users/registrations",
                "/webjars/**",
                "/swagger-ui/**",
                "/swagger-ui/index.html",
                "/swagger-resources/**",
                "/v2/api-docs/**"
            ).permitAll()
            .antMatchers("/v1/users/token/refresh").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers(HttpMethod.PUT, "/v1/users/*").hasAnyAuthority(Role.USER.authority)
            .antMatchers("/v1/**/**").hasAnyAuthority(Role.USER.authority, Role.ADMIN.authority)
            .anyRequest().authenticated()
            .and().logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/v1").permitAll()
            .and()
            // login and generate token
            .addFilter(authenticationFilter)
            // re-identify the logged user, verify token
            .addFilterBefore(AuthorizationFilter(tokenService, objectMapper), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

//    @Throws(java.lang.Exception::class)
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth
//            .userDetailsService<UserDetailsService>(userService)
//            .passwordEncoder(passwordEncoder)
//    }
}