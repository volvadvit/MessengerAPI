package com.volvadvit.messenger.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

//TODO add unit & init test for controllers
@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerMvcTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

//    @Test
//    fun test() {
//        this.mockMvc.perform(
//            get("/")
//                .with(csrf()))
//            .andExpect(status().isOk)
//    }

//    @MockBean
//    private lateinit var userService: UserServiceImpl
//
//    @MockBean
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var restTemplate: TestRestTemplate
//
//    private lateinit var testUser: User
//
//    @BeforeEach
//    fun setUp() {
//        testUser = User()
//        testUser.username = "name"
//    }
//
//    @Test
//    fun testCreate() {
//        `when`(userService.attemptRegistration(testUser)).thenReturn(testUser)
//        val json = ObjectMapper().writeValueAsString(testUser)
//        val headers = HttpHeaders()
//        headers.contentType = MediaType.APPLICATION_JSON
//        val request : HttpEntity<String> = HttpEntity(json, headers)
//
//        val result = restTemplate.postForEntity("/users/registrations", request, String::class.java)
//
//        assertEquals(HttpStatus.OK, result.statusCode)
//        assertEquals(testUser.username, result.body?.contains("name"))
//        mockMvc.perform(
//            post("/users/registrations")
//            .with(csrf())
//            .content(ObjectMapper().writeValueAsString(testUser))
//            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk)
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.username").value("name"))
//    }
//
//    @Test
//    fun testGetUserDetails() {
//        `when`(userRepository.findByUsername("name")).thenReturn(testUser)
//        mockMvc.perform(
//            get("/users/details")
//                .with(csrf()).principal { "name" })
//            .andExpect(status().isOk)
//    }

//    @Test
//    @WithMockUser(username = "")
}