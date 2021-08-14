package com.volvadvit.messenger.api.services

import com.volvadvit.messenger.api.models.User

interface UserService {
    fun attemptRegistration(userDetails: User) : User
    fun listUsers(currentUser: User) : List<User>
    fun retrieveUserData(username: String) : User?
    fun retrieveUserData(id: Long) : User?
    fun usernameExists(username: String) : Boolean
}