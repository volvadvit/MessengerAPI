package com.volvadvit.messenger.models

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER, ADMIN, DEV;

    override fun getAuthority(): String {
        return name
    }
}