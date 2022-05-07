package com.volvadvit.messenger.services

import com.volvadvit.messenger.constants.ResponseConstants.*
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UserAlreadyExists
import com.volvadvit.messenger.exceptions.UserNotExists
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import com.volvadvit.messenger.models.User
import com.volvadvit.messenger.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
@Service
class UserService (
    private val repository: UserRepository,
    private val tokenService: TokenService
) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(p0: String?): UserDetails {
        val user = p0?.let { repository.findByUsername(it) } ?:
        throw UsernameNotFoundException("User with username \'$p0\' doesn't exists")
        return org.springframework.security.core.userdetails.User(user.username, user.password, user.roles)
    }

    @Throws(UsernameUnavailableException::class)
    fun attemptRegistration(userDetails: User?): User {
        if (userDetails == null) {
            logger.info(SAVE_NULL_USER.value)
            throw java.lang.IllegalArgumentException(SAVE_NULL_USER.value)
        }
        if (usernameExists(userDetails.username)) {
            throw UserAlreadyExists("User ${userDetails.username} already exists.")
        }
        val user = User()
        user.username = userDetails.username
        user.email = userDetails.email
        user.password = userDetails.password
        user.photoUrl = userDetails.photoUrl ?: "https://i.ytimg.com/vi/dqQcGplC2eg/mqdefault.jpg"
        user.createdAt = userDetails.createdAt
        val savedUser = repository.save(user)
        logger.info("User ${savedUser.id}:${savedUser.username} saved")
        return savedUser
    }

    fun listUsersExceptOf(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }.filter { it != currentUser }
    }

    fun getByUsername(username: String?): User {
        if (username.isNullOrBlank()) {
            throw UsernameUnavailableException(USERNAME_UNAVAILABLE.value)
        }
        return repository.findByUsername(username) ?: throw UserNotExists(USER_NOT_EXISTS.value)
    }

    @Throws(InvalidUserIdException::class)
    fun getById(id: Long?): User {
        if (id == null) {
            throw InvalidUserIdException(ID_NULL.value)
        }
        val userOptional = repository.findById(id)
        return userOptional.orElseThrow {
            throw InvalidUserIdException("A user with an id of '$id' does not exist.")
        }
    }

    fun usernameExists(username: String): Boolean {
        return repository.findByUsername(username) != null
    }

    fun updateUser(currentUser: User, updateDetails: User?) : User {
        if (updateDetails == null) {
            throw InvalidUserIdException(SAVE_NULL_USER.value)
        }
        logger.info("Update user from: ${currentUser.toString()}, to: ${updateDetails.toString()}")
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
        user.password = password.fromBase64()
        repository.save(user)
    }

    fun deleteUser(user: User?) {
        if (user == null) {
            throw IllegalArgumentException("User are null")
        }
        if (usernameExists(user.username)) {
            logger.info("Delete user: ${user.username}")
            repository.delete(user)
            tokenService.deleteToken(user.username)
        } else {
            throw UsernameUnavailableException(USERNAME_UNAVAILABLE.value)
        }
    }

    fun addFriend(user: User, friendId: Long?) : User {
        if (friendId == null) {
            throw InvalidUserIdException("${ID_NULL.value}. Cannot add/find friend")
        }
        getById(friendId)
        val friends = user.friendsId as HashSet<Long>
        friends.add(friendId)
        return repository.save(user)
    }

    fun getFriendsList(user: User) = user.friendsId.map {id -> repository.findById(id).orElse(User())}
}

