package com.volvadvit.messenger.services

import com.volvadvit.messenger.api.v1.models.UpdatedUserDetails
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(p0: String?): UserDetails {
        val user = p0?.let { repository.findByUsername(it) } ?:
        throw UsernameNotFoundException("User with username \'$p0\' doesn't exists")
        return org.springframework.security.core.userdetails.User(user.username, user.password, user.roles)
    }

    @Throws(UsernameUnavailableException::class)
    fun attemptRegistration(userDetails: User): User {
        if (!usernameExists(userDetails.username)) {
            val user = User()
            user.username = userDetails.username
            user.email = userDetails.email
            user.password = userDetails.password
            repository.save(user)
            obscurePassword(user)
            return user
        }
        throw UsernameUnavailableException("The username ${userDetails.username} is unavailable.")
    }

    fun listUsers(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }.filter { it != currentUser }
    }

    fun retrieveUserData(username: String): User? {
        val user = repository.findByUsername(username)
        obscurePassword(user)
        return user
    }

    @Throws(InvalidUserIdException::class)
    fun retrieveUserData(id: Long): User? {
        val userOptional = repository.findById(id)
        if (userOptional.isPresent) {
            val user = userOptional.get()
            obscurePassword(user)
            return user
        }
        throw InvalidUserIdException("A user with an id of '$id' does not exist.")
    }

    fun usernameExists(username: String): Boolean {
        return repository.findByUsername(username) != null
    }

    fun updateUserStatus(currentUser: User, updateDetails: UpdatedUserDetails) : User {
        //TODO add validation
        if (!updateDetails.status.isNullOrBlank()) {
            currentUser.status = updateDetails.status
        }
        if (!updateDetails.username.isNullOrBlank()) {
            currentUser.username = updateDetails.username
        }
        if (!updateDetails.password.isNullOrBlank()) {
            currentUser.password = updateDetails.password
        }
        if (!updateDetails.email.isNullOrBlank()) {
            currentUser.email = updateDetails.email
        }
        repository.save(currentUser)
        return currentUser
    }

    private fun obscurePassword(user: User?) {
        user?.password = "XXX XXXX XXX"
    }
}