package com.volvadvit.messenger.security

import com.volvadvit.messenger.models.Role

class AccountCredentials {
    lateinit var username: String
    lateinit var password: String
    lateinit var roles: Set<Role>
}