package com.volvadvit.messenger.services

import com.volvadvit.messenger.constants.ResponseConstants.ILLEGAL_PASSWORD
import com.volvadvit.messenger.constants.ResponseConstants.USERNAME_UNAVAILABLE
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UserNotExists
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val repository: UserRepository,
    private val tokenService: TokenService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(p0: String?): UserDetails {
        val user = p0?.let { repository.findByUsername(it) } ?:
        throw UsernameNotFoundException("User with username \'$p0\' doesn't exists")
        return org.springframework.security.core.userdetails.User(user.username, user.password, user.roles)
    }

    @Throws(UsernameUnavailableException::class)
    fun attemptRegistration(userDetails: User?): User {
        if (userDetails == null) {
            throw java.lang.IllegalArgumentException("Cannot save null except of user")
        }
        if (!usernameExists(userDetails.username)) {
            val user = User()
            user.username = userDetails.username
            user.email = userDetails.email
            user.password = userDetails.password
            user.photoUrl = userDetails.photoUrl
            user.createdAt = userDetails.createdAt
            repository.save(user)
            return user
        }
        throw UsernameUnavailableException("The username ${userDetails.username} is unavailable.")
    }

    fun listUsersExceptOf(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }.filter { it != currentUser }
    }

    fun getByUsername(username: String?): User {
        if (username.isNullOrBlank()) {
            throw UsernameUnavailableException(USERNAME_UNAVAILABLE.value)
        }
        return repository.findByUsername(username) ?: throw UserNotExists("Not found user for provided username")
    }

    @Throws(InvalidUserIdException::class)
    fun getById(id: Long): User {
        val userOptional = repository.findById(id)
        if (userOptional.isPresent) {
            return userOptional.get()
        }
        throw InvalidUserIdException("A user with an id of '$id' does not exist.")
    }

    fun usernameExists(username: String): Boolean {
        return repository.findByUsername(username) != null
    }

    fun updateUser(currentUser: User, updateDetails: User) : User {
        if (updateDetails.username.isNotBlank()) {
            currentUser.username = updateDetails.username
        }
        if (updateDetails.email.isNotBlank()) {
            currentUser.email = updateDetails.email
        }
        if (updateDetails.photoUrl?.isNotBlank() == true) {
            currentUser.photoUrl = updateDetails.photoUrl
        }
        repository.save(currentUser)
        return currentUser
    }

    fun updateUserPassword(user: User, password: String?) {
        if (password.isNullOrBlank()) {
            throw IllegalArgumentException(ILLEGAL_PASSWORD.value)
        }
        if (password.length < 3) {
            throw IllegalArgumentException(ILLEGAL_PASSWORD.value)
        }
        val decodedPassword = Base64.getDecoder().decode(password)
        user.password = decodedPassword.toString()
        repository.save(user)
    }

    fun deleteUser(user: User?) {
        if (user == null) {
            throw IllegalArgumentException("User are null")
        }
        if (usernameExists(user.username)) {
            repository.delete(user)
            tokenService.deleteToken(user.username)
        } else {
            throw UsernameUnavailableException(USERNAME_UNAVAILABLE.value)
        }
    }
}