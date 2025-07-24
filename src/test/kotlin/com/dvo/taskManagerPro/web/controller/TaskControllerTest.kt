package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.TaskManagerProApplication
import com.dvo.taskManagerPro.configuration.SecurityConfiguration
import com.dvo.taskManagerPro.entity.*
import com.dvo.taskManagerPro.mapper.TaskMapper
import com.dvo.taskManagerPro.security.UserDetailsServiceImpl
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.service.TaskService
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import com.dvo.taskManagerPro.web.model.request.UpsertTaskRequest
import com.dvo.taskManagerPro.web.model.response.TaskResponse
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
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(TaskController::class)
@Import(SecurityConfiguration::class, UserDetailsServiceImpl::class)
@ContextConfiguration(classes = [TaskManagerProApplication::class])
class TaskControllerTest {
    @MockBean
    private lateinit var taskService: TaskService

    @MockBean
    private lateinit var taskMapper: TaskMapper

    @MockBean
    private lateinit var projectService: ProjectService

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var task: Task
    private lateinit var taskResponse: TaskResponse
    private lateinit var project: Project
    private val url = "/api/tasks"

    @BeforeEach
    fun setUp() {
        project = Project(
            id = 1L,
            name = "name",
            description = "",
            createdAt = LocalDateTime.now(),
            users = mutableListOf(),
            tasks = mutableListOf()
        )

        task = Task(
            id = 1L,
            title = "title",
            description = "description",
            createdAt = LocalDateTime.now(),
            dueDate = LocalDate.of(2025, 7, 10),
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            project = project,
            comments = mutableListOf(),
            labels = mutableListOf(),
            assignedTo = null
        )

        taskResponse = TaskResponse(
            id = 1L,
            title = "title",
            description = "description",
            createdAt = LocalDateTime.now(),
            dueDate = LocalDate.of(2025, 7, 10),
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            username = "username",
            projectId = 1L
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findAllByFilter`() {
        val filter = TaskFilter(pageNumber = 0, pageSize = 10)

        whenever(taskService.findAllByFilter(filter)).thenReturn(listOf(task))
        whenever(taskMapper.taskToResponse(task)).thenReturn(taskResponse)

        mockMvc.perform(
            get(url)
                .param("pageNumber", filter.pageNumber.toString())
                .param("pageSize", filter.pageSize.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value(1L))

        verify(taskService).findAllByFilter(filter)
        verify(taskMapper).taskToResponse(task)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findById`() {
        whenever(taskService.findById(1L)).thenReturn(task)
        whenever(taskMapper.taskToResponse(task)).thenReturn(taskResponse)

        mockMvc.perform(
            get("$url/id/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))

        verify(taskService).findById(1L)
        verify(taskMapper).taskToResponse(task)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test create`() {
        val request = UpsertTaskRequest(
            title = "title",
            description = "description",
            dueDate = LocalDate.of(2025, 7, 10),
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM
        )

        whenever(projectService.findById(1L)).thenReturn(project)
        whenever(taskMapper.requestToTask(request, project)).thenReturn(task)
        whenever(taskService.create(task)).thenReturn(task)
        whenever(taskMapper.taskToResponse(task)).thenReturn(taskResponse)

        mockMvc.perform(
            post("$url/create")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .param("projectId", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))

        verify(projectService).findById(1L)
        verify(taskMapper).requestToTask(request, project)
        verify(taskService).create(task)
        verify(taskMapper).taskToResponse(task)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test update`() {
        val request = UpdateTaskRequest(
            title = "title",
            description = "description",
            dueDate = LocalDate.now(),
            status = TaskStatus.TODO,
            priority = TaskPriority.LOW
        )

        whenever(taskService.update(request, 1L)).thenReturn(task)
        whenever(taskMapper.taskToResponse(task)).thenReturn(taskResponse)

        mockMvc.perform(
            put("$url/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))

        verify(taskService).update(request, 1L)
        verify(taskMapper).taskToResponse(task)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test deleteById`() {
        doNothing().whenever(taskService).deleteById(1L)

        mockMvc.perform(delete("$url/1"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test assignTaskToUser`() {
        doNothing().whenever(taskService).assignTaskToUser("username", 1L)

        mockMvc.perform(
            put("$url/id/1/user")
                .param("username", "username")
        )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test unassignTaskFromUser`() {
        doNothing().whenever(taskService).unassignTaskFromUser(1L)

        mockMvc.perform(
            put("$url/id/1/delUser")
                .param("username", "username")
        )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test assignLabelToTask`() {
        doNothing().whenever(taskService).assignLabelToTask(1L, 1L)

        mockMvc.perform(
            put("$url/id/1/label")
                .param("labelId", "1")
        )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test unassignLabelFromTask`() {
        doNothing().whenever(taskService).unassignLabelFromTask(1L, 1L)

        mockMvc.perform(
            put("$url/id/1/delLabel")
                .param("labelId", "1")
        )
            .andExpect(status().isOk)
    }

}