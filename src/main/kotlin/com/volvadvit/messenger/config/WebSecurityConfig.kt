package com.volvadvit.messenger.config

import com.volvadvit.messenger.filter.AuthenticationFilter
import com.volvadvit.messenger.filter.AuthorizationFilter
import com.volvadvit.messenger.models.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val authenticationFilter = AuthenticationFilter(authenticationManagerBean()!!)
        authenticationFilter.setFilterProcessesUrl("/api/login")

        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
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
            .addFilter(authenticationFilter)
            .addFilterBefore(AuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}