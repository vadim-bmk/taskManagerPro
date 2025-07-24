package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.*
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.TaskMapper
import com.dvo.taskManagerPro.repository.LabelRepository
import com.dvo.taskManagerPro.repository.TaskRepository
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class TaskServiceImplTest {
    private lateinit var taskService: TaskServiceImpl
    private val taskRepository: TaskRepository = mock()
    private val userRepository: UserRepository = mock()
    private val labelRepository: LabelRepository = mock()
    private val taskMapper: TaskMapper = mock()

    private lateinit var task: Task
    private lateinit var user: User
    private lateinit var label: Label

    @BeforeEach
    fun setUp() {
        taskService = TaskServiceImpl(taskRepository, userRepository, labelRepository, taskMapper)

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

        user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        label = Label(
            id = 1L,
            name = "label",
            tasks = mutableListOf()
        )
    }

    @Test
    fun `test findAll`() {
        whenever(taskRepository.findAll()).thenReturn(listOf(task))

        val result = taskService.findAll()

        assertEquals(1, result.size)
        verify(taskRepository).findAll()
    }

    @Test
    fun `test findAllByFilter`() {
        val filter = TaskFilter(pageNumber = 0, pageSize = 10)
        val taskList = listOf(task)
        val page = PageImpl(taskList, PageRequest.of(filter.pageNumber!!, filter.pageSize!!), taskList.size.toLong())

        whenever(
            taskRepository.findAll(
                any<Specification<Task>>(),
                eq(PageRequest.of(filter.pageNumber!!, filter.pageSize!!))
            )
        ).thenReturn(page)

        val result = taskService.findAllByFilter(filter)

        assertEquals(1, result.size)
        assertEquals(taskList, result)
    }

    @Test
    fun `test findById`() {
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))

        val result = taskService.findById(1L)

        assertEquals(task, result)
        verify(taskRepository).findById(1L)
    }

    @Test
    fun `test findById when task not found`() {
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.findById(1L)
        }
    }

    @Test
    fun `test create`() {
        whenever(taskRepository.existsByTitle("title")).thenReturn(false)
        whenever(taskRepository.save(task)).thenReturn(task)

        val result = taskService.create(task)

        assertEquals(task, result)
        verify(taskRepository).existsByTitle("title")
        verify(taskRepository).save(task)
    }

    @Test
    fun `test create when task already exists`() {
        whenever(taskRepository.existsByTitle("title")).thenReturn(true)

        assertThrows<EntityExistsException> {
            taskService.create(task)
        }
    }

    @Test
    fun `test update`() {
        val request = UpdateTaskRequest(
            title = "title",
            description = "descr",
            dueDate = LocalDate.of(2025, 7, 1),
            status = TaskStatus.DONE,
            priority = TaskPriority.LOW
        )
        val existedTask = task.copy()

        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(existedTask))
        whenever(taskRepository.existsByTitle("title")).thenReturn(false)
        whenever(taskRepository.save(existedTask)).thenReturn(existedTask)

        val result = taskService.update(request, 1L)

        assertEquals(existedTask, result)
        verify(taskRepository).findById(1L)
        verify(taskRepository).existsByTitle("title")
        verify(taskRepository).save(existedTask)
    }

    @Test
    fun `test update task when task not found`() {
        val request = UpdateTaskRequest(
            title = "title",
            description = "descr",
            dueDate = LocalDate.of(2025, 7, 1),
            status = TaskStatus.DONE,
            priority = TaskPriority.LOW
        )
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.update(request, 1L)
        }
    }

    @Test
    fun `test update task when title is already exists`() {
        val request = UpdateTaskRequest(
            title = "new title",
            description = "descr",
            dueDate = LocalDate.of(2025, 7, 1),
            status = TaskStatus.DONE,
            priority = TaskPriority.LOW
        )

        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(taskRepository.existsByTitle("new title")).thenReturn(true)

        assertThrows<EntityExistsException> {
            taskService.update(request, 1L)
        }
    }

    @Test
    fun `test deleteById`() {
        taskService.deleteById(1L)

        verify(taskRepository).deleteById(1L)
    }

    @Test
    fun `test assignTaskToUser`() {
        whenever(userRepository.findByUsername("user")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(taskRepository.save(task)).thenReturn(task)

        val result = taskService.assignTaskToUser("user", 1L)

        verify(userRepository).findByUsername("user")
        verify(taskRepository).findById(1L)
    }

    @Test
    fun `test assignTaskToUser when user not found`() {
        whenever(userRepository.findByUsername("user")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignTaskToUser("user", 1L)
        }
    }

    @Test
    fun `test assignTaskToUser when task not found`() {
        whenever(userRepository.findByUsername("user")).thenReturn(Optional.of(user))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignTaskToUser("user", 1L)
        }
    }

    @Test
    fun `test unassignTaskFromUser`() {
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(taskRepository.save(task)).thenReturn(task)

        val result = taskService.unassignTaskFromUser(1L)

        verify(taskRepository).findById(1L)
        verify(taskRepository).save(task)
    }

    @Test
    fun `test unassignTaskFromUser when task not found`() {
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.unassignTaskFromUser(1L)
        }
    }

    @Test
    fun `test getTasksByUser`() {
        whenever(taskRepository.findByAssignedToId(1L)).thenReturn(listOf(task))

        val result = taskService.getTasksByUser(1L)

        assertEquals(1, result.size)
        verify(taskRepository).findByAssignedToId(1L)
    }

    @Test
    fun `test assignLabelToTask`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(taskRepository.save(task)).thenReturn(task)

        val result = taskService.assignLabelToTask(1L, 1L)

        verify(labelRepository).findById(1L)
        verify(taskRepository).findById(1L)
        verify(taskRepository).save(task)
    }

    @Test
    fun `test assignLabelToTask when label not found`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignLabelToTask(1L, 1L)
        }
    }

    @Test
    fun `test assignLabelToTask when task not found`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignLabelToTask(1L, 1L)
        }
    }

    @Test
    fun `test unassignLabelToTask`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        whenever(taskRepository.save(task)).thenReturn(task)

        val result = taskService.assignLabelToTask(1L, 1L)

        verify(labelRepository).findById(1L)
        verify(taskRepository).findById(1L)
        verify(taskRepository).save(task)
    }

    @Test
    fun `test unassignLabelToTask when label not found`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignLabelToTask(1L, 1L)
        }
    }

    @Test
    fun `test unassignLabelToTask when task not found`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))
        whenever(taskRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            taskService.assignLabelToTask(1L, 1L)
        }
    }
}