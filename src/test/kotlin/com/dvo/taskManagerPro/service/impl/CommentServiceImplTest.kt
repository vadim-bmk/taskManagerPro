package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.*
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.CommentMapper
import com.dvo.taskManagerPro.repository.CommentRepository
import com.dvo.taskManagerPro.repository.TaskRepository
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class CommentServiceImplTest {
    private lateinit var commentService: CommentServiceImpl
    private val commentRepository: CommentRepository = mock()
    private val userRepository: UserRepository = mock()
    private val taskRepository: TaskRepository = mock()
    private val commentMapper: CommentMapper = mock()

    private lateinit var comment: Comment
    private lateinit var user: User
    private lateinit var task: Task
    private lateinit var request: UpsertCommentRequest
    private lateinit var updateRequest: UpdateCommentRequest

    @BeforeEach
    fun setUp() {
        commentService = CommentServiceImpl(commentRepository, userRepository, taskRepository, commentMapper)

        user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        task = Task(
            id = 1L,
            title = "title",
            description = "description",
            createdAt = LocalDateTime.now(),
            dueDate = LocalDate.of(2025, 7, 1),
            status = TaskStatus.TODO,
            priority = TaskPriority.LOW,
            assignedTo = null,
            project = Project(
                1L, "projectName", "projectDescription", LocalDateTime.now(),
                users = mutableListOf(),
                tasks = mutableListOf()
            ),
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

        request = UpsertCommentRequest(
            text = "text",
            username = "username",
            taskId = 1L
        )

        updateRequest = UpdateCommentRequest(
            text = "update text",
            username = "username",
            taskId = 1L
        )
    }

    @Test
    fun `test findAll`() {
        val commentList = listOf(comment)
        val page = PageImpl(commentList, PageRequest.of(0, 10), commentList.size.toLong())

        whenever(commentRepository.findAll(PageRequest.of(0, 10))).thenReturn(page)

        val result = commentService.findAll(PageRequest.of(0, 10))

        assertEquals(1, result.content.size)
        verify(commentRepository).findAll(PageRequest.of(0, 10))
    }

    @Test
    fun `test findById`() {
        whenever(commentRepository.findById(1L)).thenReturn(Optional.of(comment))

        val result = commentService.findById(1L)

        assertEquals(comment, result)
        verify(commentRepository).findById(1L)
    }

    @Test
    fun `test findById when comment not found`() {
        whenever(commentRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.findById(1L)
        }
    }

    @Test
    fun `test create`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(commentMapper.requestToComment(request, user, task)).thenReturn(comment)
        whenever(commentRepository.save(comment)).thenReturn(comment)

        val result = commentService.create(request)

        assertEquals(comment, result)
        verify(userRepository).findByUsername("username")
        verify(taskRepository).findById(1L)
        verify(commentMapper).requestToComment(request, user, task)
        verify(commentRepository).save(comment)
    }

    @Test
    fun `test create when user not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.create(request)
        }
    }

    @Test
    fun `test create when task not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.create(request)
        }
    }

    @Test
    fun `test update`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(commentRepository.findById(1L)).thenReturn(Optional.of(comment))
        doNothing().whenever(commentMapper).updateRequestToComment(updateRequest, user, task, comment)
        whenever(commentRepository.save(comment)).thenReturn(comment)

        val result = commentService.update(updateRequest, 1L)

        assertEquals(comment, result)
        verify(userRepository).findByUsername("username")
        verify(taskRepository).findById(1L)
        verify(commentRepository).findById(1L)
        verify(commentRepository).save(comment)
    }

    @Test
    fun `test update when user not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.update(updateRequest, 1L)
        }
    }

    @Test
    fun `test update when task not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.update(updateRequest, 1L)
        }
    }

    @Test
    fun `test update when comment not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(commentRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            commentService.update(updateRequest, 1L)
        }
    }

    @Test
    fun `test deleteById`() {
        commentService.deleteById(1L)

        verify(commentRepository).deleteById(1L)
    }
}