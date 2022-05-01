package com.volvadvit.messenger.config

import com.volvadvit.messenger.filters.JWTAuthenticationFilter
import com.volvadvit.messenger.filters.JWTLoginFilter
import com.volvadvit.messenger.models.Role
import com.volvadvit.messenger.services.impl.AppUserDetailsService
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
    val appUserDetailsService: AppUserDetailsService,
    val passwordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers( HttpMethod.POST, "/api/v1/users/registrations").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/h2-console/**/**").hasAnyAuthority(Role.DEV.authority, Role.ADMIN.authority)
            .antMatchers(
                "/webjars/**",
                "/swagger-ui/**",
                "/swagger-ui/index.html",
                "/swagger-resources/**",
                "/v2/api-docs/**").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/v1/users/*").hasAnyAuthority(Role.DEV.authority, Role.ADMIN.authority)
            .antMatchers("/api/v1/**").hasAnyAuthority(Role.USER.authority, Role.ADMIN.authority)
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
        auth
            ?.userDetailsService<UserDetailsService>(appUserDetailsService)
            ?.passwordEncoder(passwordEncoder)
    }
}