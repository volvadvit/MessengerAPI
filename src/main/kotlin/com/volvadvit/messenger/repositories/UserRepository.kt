package com.volvadvit.messenger.repositories

import com.volvadvit.messenger.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String) : User?
    fun findByPhoneNumber(phoneNumber: String) : User?
}