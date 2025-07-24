package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.TaskManagerProApplication
import com.dvo.taskManagerPro.configuration.SecurityConfiguration
import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.mapper.UserMapper
import com.dvo.taskManagerPro.security.UserDetailsServiceImpl
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import com.dvo.taskManagerPro.web.model.request.UpsertUserRequest
import com.dvo.taskManagerPro.web.model.response.UserResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
@Import(SecurityConfiguration::class, UserDetailsServiceImpl::class)
@ContextConfiguration(classes = [TaskManagerProApplication::class])
class UserControllerTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var user: User
    private lateinit var userResponse: UserResponse
    private val url = "/api/users"

    @BeforeEach
    fun setUp() {
        user = User(
            id = 1L,
            username = "username",
            email = "email@mailru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        userResponse = UserResponse(
            username = "username",
            email = "email@mail.ru",
            role = RoleType.ROLE_ADMIN.name
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findAll by Admin`() {
        val paginationRequest = PaginationRequest(
            pageSize = 10,
            pageNumber = 0
        )
        val userList = listOf(user)
        val page = PageImpl(userList, PageRequest.of(0, 10), userList.size.toLong())
        whenever(userService.findAll(PageRequest.of(0, 10))).thenReturn(page)
        whenever(userMapper.userToResponse(user)).thenReturn(userResponse)

        mockMvc.perform(
            get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginationRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].username").value("username"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_EMPLOYEE"])
    fun `test findAll by Employee`() {
        val paginationRequest = PaginationRequest(
            pageSize = 10,
            pageNumber = 0
        )

        mockMvc.perform(
            get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginationRequest))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findByUsername by Admin`() {
        whenever(userService.findByUsername("username")).thenReturn(user)
        whenever(userMapper.userToResponse(user)).thenReturn(userResponse)

        mockMvc.perform(get("$url/username/username"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("username"))
    }

    @Test
    fun `test create`() {
        val request = UpsertUserRequest(
            username = "username",
            email = "email@mail.ru",
            password = "12345"
        )

        whenever(userMapper.requestToUser(request, RoleType.ROLE_ADMIN)).thenReturn(user)
        whenever(userService.create(any<User>())).thenReturn(user)
        whenever(userMapper.userToResponse(user)).thenReturn(userResponse)

        mockMvc.perform(
            post("$url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("roleType", "ROLE_ADMIN")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("username"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test update by Admin`() {
        val request = UpdateUserRequest(
            password = "12345",
            email = "new@mail.ru"
        )

        whenever(userService.update(request, "username")).thenReturn(user)
        whenever(userMapper.userToResponse(user)).thenReturn(userResponse)

        mockMvc.perform(
            put("$url/update/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("username"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test delete by Admin`() {
        doNothing().whenever(userService).deleteByUsername("username")

        mockMvc.perform(delete("$url/username"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_EMPLOYEE"])
    fun `test delete by Employee`() {
        mockMvc.perform(delete("$url/username"))
            .andExpect(status().isForbidden)
    }
}