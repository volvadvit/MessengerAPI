package com.volvadvit.messenger.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.RestTemplate

@Configuration
class CommonBeansConfig {

    @Bean
    fun getPasswordEncoder() : BCryptPasswordEncoder = BCryptPasswordEncoder(8)

    @Bean
    fun getRestTemplate() : RestTemplate = RestTemplate()

    @Bean
    fun getObjectMapper() : ObjectMapper = jacksonObjectMapper()
}