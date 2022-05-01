package com.volvadvit.messenger.services.impl

import com.volvadvit.messenger.api.v1.models.UpdatedUserDetails
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import com.volvadvit.messenger.services.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val repository: UserRepository) : UserService {

    @Throws(UsernameUnavailableException::class)
    override fun attemptRegistration(userDetails: User): User {
        if (!usernameExists(userDetails.username)) {
            val user = User()
            user.username = userDetails.username
            user.phoneNumber = userDetails.phoneNumber
            user.password = userDetails.password
            repository.save(user)
            obscurePassword(user)
            return user
        }
        throw UsernameUnavailableException("The username ${userDetails.username} is unavailable.")
    }

    override fun listUsers(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }.filter { it != currentUser }
    }

    override fun retrieveUserData(username: String): User? {
        val user = repository.findByUsername(username)
        obscurePassword(user)
        return user
    }

    @Throws(InvalidUserIdException::class)
    override fun retrieveUserData(id: Long): User? {
        val userOptional = repository.findById(id)
        if (userOptional.isPresent) {
            val user = userOptional.get()
            obscurePassword(user)
            return user
        }
        throw InvalidUserIdException("A user with an id of '$id' does not exist.")
    }

    override fun usernameExists(username: String): Boolean {
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
        if (!updateDetails.phoneNumber.isNullOrBlank()) {
            currentUser.phoneNumber = updateDetails.phoneNumber
        }
        repository.save(currentUser)
        return currentUser
    }

    private fun obscurePassword(user: User?) {
        user?.password = "XXX XXXX XXX"
    }
}