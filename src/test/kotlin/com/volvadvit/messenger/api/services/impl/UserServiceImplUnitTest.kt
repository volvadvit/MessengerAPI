package com.volvadvit.messenger.api.services.impl

import com.volvadvit.messenger.api.models.User
import com.volvadvit.messenger.api.repositories.UserRepository
import com.volvadvit.messenger.api.services.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class UserServiceImplUnitTest {

    private val repository: UserRepository = mock(UserRepository::class.java)
    private val userService: UserService = UserServiceImpl(repository)
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        testUser = User(username = "name")
    }

    @Test
    fun testUsernameExists() {
        `when`(repository.findByUsername("name")).thenReturn(testUser)
        val result = userService.usernameExists("name")
        assertEquals(true, result)
    }
}