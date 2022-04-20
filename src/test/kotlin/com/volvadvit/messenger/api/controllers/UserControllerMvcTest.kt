package com.volvadvit.messenger.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.api.models.User
import com.volvadvit.messenger.api.repositories.UserRepository
import com.volvadvit.messenger.api.services.UserService
import com.volvadvit.messenger.api.services.impl.AppUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(UserController::class)
@ContextConfiguration(classes = arrayOf(UserRepository::class, AppUserDetailsService::class))
internal class UserControllerMvcTest @Autowired constructor(private val ctx: WebApplicationContext) {

    @MockBean
    private lateinit var userService: UserService
    private lateinit var mockMvc: MockMvc
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(ctx)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build();

        testUser = User(username = "name")
    }

    @Test
    fun testCreate() {
        `when`(userService.attemptRegistration(testUser)).thenReturn(testUser)
        mockMvc.perform(post("/users/registrations")
            .content(ObjectMapper().writeValueAsString(testUser))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value("name"))
    }

    @Test
    fun testErrorResponseEchoDetails() {
        `when`(userService.attemptRegistration(testUser)).thenReturn(testUser)
        mockMvc.perform(get("/users/details"))
            .andExpect(status().isBadRequest)
    }

//    @Test
//    @WithMockUser(username = "")
}