package com.volvadvit.messenger.api.config

import com.volvadvit.messenger.api.filters.JWTAuthenticationFilter
import com.volvadvit.messenger.api.filters.JWTLoginFilter
import com.volvadvit.messenger.api.services.impl.AppUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig (
    val appUserDetailsService: AppUserDetailsService
    ) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/users/registrations")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/login")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(
                // set jwt token if credentials are ok
                JWTLoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                // check jwt token
                JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService<UserDetailsService>(appUserDetailsService)
            ?.passwordEncoder(getPasswordEncoder())
    }

    @Bean
    fun getPasswordEncoder() : BCryptPasswordEncoder = BCryptPasswordEncoder(8)
}