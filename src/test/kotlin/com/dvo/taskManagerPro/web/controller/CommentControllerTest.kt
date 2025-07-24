package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.TaskManagerProApplication
import com.dvo.taskManagerPro.configuration.SecurityConfiguration
import com.dvo.taskManagerPro.entity.*
import com.dvo.taskManagerPro.mapper.CommentMapper
import com.dvo.taskManagerPro.security.UserDetailsServiceImpl
import com.dvo.taskManagerPro.service.CommentService
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import com.dvo.taskManagerPro.web.model.response.CommentResponse
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(CommentController::class)
@Import(SecurityConfiguration::class, UserDetailsServiceImpl::class)
@ContextConfiguration(classes = [TaskManagerProApplication::class])
class CommentControllerTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var commentService: CommentService

    @MockBean
    private lateinit var commentMapper: CommentMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var comment: Comment
    private lateinit var commentResponse: CommentResponse
    private val url = "/api/comments"

    @BeforeEach
    fun setUp() {
        val user = User(
            id = 1L,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        val project = Project(
            id = 1L,
            name = "name",
            description = "",
            createdAt = LocalDateTime.now(),
            users = mutableListOf(),
            tasks = mutableListOf()
        )

        val task = Task(
            id = 1L,
            title = "title",
            description = "description",
            createdAt = LocalDateTime.now(),
            dueDate = LocalDate.now(),
            status = TaskStatus.TODO,
            priority = TaskPriority.LOW,
            assignedTo = user,
            project = project,
            comments = mutableListOf(),
            labels = mutableListOf()
        )

        comment = Comment(
            id = 1L,
            text = "text",
            createdAt = LocalDateTime.now(),
            author = user,
            task = task
        )

        commentResponse = CommentResponse(
            id = 1L,
            text = "text",
            username = "username",
            taskId = 1L
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findAll`() {
        val comments = listOf(comment)
        val page = PageImpl(comments, PageRequest.of(0, 10), comments.size.toLong())
        val request = PaginationRequest(
            pageSize = 10,
            pageNumber = 0
        )

        whenever(commentService.findAll(PageRequest.of(0, 10))).thenReturn(page)
        whenever(commentMapper.commentToResponse(comment)).thenReturn(commentResponse)

        mockMvc.perform(
            get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value("1"))

        verify(commentService).findAll(PageRequest.of(0, 10))
        verify(commentMapper).commentToResponse(comment)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findById`() {
        whenever(commentService.findById(1L)).thenReturn(comment)
        whenever(commentMapper.commentToResponse(comment)).thenReturn(commentResponse)

        mockMvc.perform(get("$url/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(commentService).findById(1L)
        verify(commentMapper).commentToResponse(comment)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test create`() {
        val request = UpsertCommentRequest(
            text = "text",
            username = "username",
            taskId = 1L
        )

        whenever(commentService.create(request)).thenReturn(comment)
        whenever(commentMapper.commentToResponse(comment)).thenReturn(commentResponse)

        mockMvc.perform(
            post("$url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(commentService).create(request)
        verify(commentMapper).commentToResponse(comment)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test update`() {
        val request = UpdateCommentRequest(
            text = "text",
            username = "username",
            taskId = 1L
        )

        whenever(commentService.update(request, 1L)).thenReturn(comment)
        whenever(commentMapper.commentToResponse(comment)).thenReturn(commentResponse)

        mockMvc.perform(
            put("$url/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(commentService).update(request, 1L)
        verify(commentMapper).commentToResponse(comment)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test deleteById`() {
        doNothing().whenever(commentService).deleteById(1L)

        mockMvc.perform(delete("$url/1"))
            .andExpect(status().isOk)
    }

}