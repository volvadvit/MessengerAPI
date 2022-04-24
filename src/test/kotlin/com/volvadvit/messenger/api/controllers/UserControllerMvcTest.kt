package com.volvadvit.messenger.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.volvadvit.messenger.api.models.User
import com.volvadvit.messenger.api.repositories.UserRepository
import com.volvadvit.messenger.api.services.impl.AppUserDetailsService
import com.volvadvit.messenger.api.services.impl.UserServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

//@WebMvcTest(UserController::class)
//@Import(AppUserDetailsService::class)
//@ContextConfiguration(classes = arrayOf(AppUserDetailsService::class))
@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerMvcTest {

    @MockBean
    private lateinit var userService: UserServiceImpl

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var ctx: WebApplicationContext

    private lateinit var mockMvc: MockMvc
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(ctx)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build();

        testUser = User()
        testUser.username = "name"
    }

    @Test
    fun testCreate() {
        `when`(userService.attemptRegistration(testUser)).thenReturn(testUser)
        mockMvc.perform(
            post("/users/registrations")
            .with(csrf())
            .content(ObjectMapper().writeValueAsString(testUser))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value("name"))
    }

    @Test
    fun testGetUserDetails() {
        `when`(userRepository.findByUsername("name")).thenReturn(testUser)
        mockMvc.perform(
            get("/users/details")
                .with(csrf()).principal { "name" })
            .andExpect(status().isOk)
    }

//    @Test
//    @WithMockUser(username = "")
}