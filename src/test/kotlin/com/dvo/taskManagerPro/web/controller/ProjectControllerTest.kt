package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.TaskManagerProApplication
import com.dvo.taskManagerPro.configuration.SecurityConfiguration
import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.mapper.ProjectMapper
import com.dvo.taskManagerPro.security.UserDetailsServiceImpl
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import com.dvo.taskManagerPro.web.model.request.UpsertProjectRequest
import com.dvo.taskManagerPro.web.model.response.ProjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ProjectController::class)
@Import(SecurityConfiguration::class, UserDetailsServiceImpl::class)
@ContextConfiguration(classes = [TaskManagerProApplication::class])
class ProjectControllerTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var projectService: ProjectService

    @MockBean
    private lateinit var projectMapper: ProjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var project: Project
    private lateinit var projectResponse: ProjectResponse
    private val url = "/api/projects"

    @BeforeEach
    fun setUp() {
        project = Project(
            id = 1L,
            name = "name",
            description = "description",
            createdAt = LocalDateTime.now(),
            users = mutableListOf(),
            tasks = mutableListOf()
        )

        projectResponse = ProjectResponse(
            id = 1L,
            name = "name",
            description = "description",
            createdAt = LocalDateTime.now(),
            tasksId = mutableListOf(),
            usersUsername = mutableListOf()
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findAllByFilter`() {
        val filter = ProjectFilter(pageNumber = 0, pageSize = 10)
        whenever(projectService.findAllByFilter(filter)).thenReturn(listOf(project))
        whenever(projectMapper.projectToResponse(project)).thenReturn(projectResponse)

        mockMvc.perform(
            get(url)
                .param("pageNumber", filter.pageNumber.toString())
                .param("pageSize", filter.pageSize.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value("1"))

        verify(projectService).findAllByFilter(filter)
        verify(projectMapper).projectToResponse(project)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findById`() {
        whenever(projectService.findById(1L)).thenReturn(project)
        whenever(projectMapper.projectToResponse(project)).thenReturn(projectResponse)

        mockMvc.perform(get("$url/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("name"))

        verify(projectService).findById(1L)
        verify(projectMapper).projectToResponse(project)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test create`() {
        val request = UpsertProjectRequest(
            name = "name",
            description = "description"
        )
        whenever(projectMapper.requestToProject(request)).thenReturn(project)
        whenever(projectService.create(project)).thenReturn(project)
        whenever(projectMapper.projectToResponse(project)).thenReturn(projectResponse)

        mockMvc.perform(
            post("$url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("name"))

        verify(projectMapper).requestToProject(request)
        verify(projectService).create(project)
        verify(projectMapper).projectToResponse(project)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test update`() {
        val request = UpdateProjectRequest(
            name = "name",
            description = "description"
        )

        whenever(projectService.update(request, 1L)).thenReturn(project)
        whenever(projectMapper.projectToResponse(project)).thenReturn(projectResponse)

        mockMvc.perform(
            put("$url/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("name"))

        verify(projectService).update(request, 1L)
        verify(projectMapper).projectToResponse(project)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test delete `() {
        doNothing().whenever(projectService).deleteById(1L)

        mockMvc.perform(delete("$url/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test assignedUserToProject`() {
        doNothing().whenever(projectService).assignedUserToProject(1L, "username")

        mockMvc.perform(put("$url/1/add/username"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test unassignedUserFromProject`() {
        doNothing().whenever(projectService).unassignedUserFromProject(1L, "username")

        mockMvc.perform(put("$url/1/delete/username"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test getProjectsByUser`() {
        whenever(projectService.getProjectsByUser("username")).thenReturn(listOf(project))
        whenever(projectMapper.projectToResponse(project)).thenReturn(projectResponse)

        mockMvc.perform(get("$url/user/username"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value("1"))
    }
}