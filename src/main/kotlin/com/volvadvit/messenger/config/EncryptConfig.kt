package com.volvadvit.messenger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class EncryptConfig {
    @Bean
    fun getPasswordEncoder() : BCryptPasswordEncoder = BCryptPasswordEncoder(8)
}