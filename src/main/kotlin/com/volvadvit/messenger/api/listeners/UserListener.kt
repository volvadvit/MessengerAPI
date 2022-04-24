package com.volvadvit.messenger.api.listeners

import com.volvadvit.messenger.api.models.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

class UserListener(private val passwordEncoder: BCryptPasswordEncoder) {
    @PrePersist
    @PreUpdate
    fun hashPassword(user: User) {
        user.password = passwordEncoder.encode(user.password)
    }
}
