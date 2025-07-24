package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.ProjectMapper
import com.dvo.taskManagerPro.repository.ProjectRepository
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectServiceImplTest {
    private lateinit var projectService: ProjectServiceImpl
    private val projectRepository: ProjectRepository = mock()
    private val userRepository: UserRepository = mock()
    private val projectMapper: ProjectMapper = mock()

    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        projectService = ProjectServiceImpl(projectRepository, userRepository, projectMapper)

        project = Project(
            id = 1L,
            name = "project",
            description = "description",
            createdAt = LocalDateTime.now(),
            users = mutableListOf(),
            tasks = mutableListOf()
        )
    }

    @Test
    fun `test findAll`() {
        whenever(projectRepository.findAll()).thenReturn(listOf(project))

        val result = projectService.findAll()

        assertEquals(1, result.size)
        verify(projectRepository).findAll()
    }

    @Test
    fun `test findAllByFilter`() {
        val filter = ProjectFilter(pageNumber = 0, pageSize = 10)
        val projectList = listOf(project)
        val page =
            PageImpl(projectList, PageRequest.of(filter.pageNumber!!, filter.pageSize!!), projectList.size.toLong())

        whenever(
            projectRepository.findAll(
                any<Specification<Project>>(),
                eq(PageRequest.of(filter.pageNumber!!, filter.pageSize!!))
            )
        ).thenReturn(page)

        val result = projectService.findAllByFilter(filter)

        assertEquals(1, result.size)
    }

    @Test
    fun `test findById `() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))

        val result = projectService.findById(1L)

        assertEquals(project, result)
        verify(projectRepository).findById(1L)
    }

    @Test
    fun `test findById when project not found`() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.findById(1L)
        }
    }

    @Test
    fun `test existsByName`() {
        whenever(projectRepository.existsByName("project")).thenReturn(true)

        val result = projectService.existsByName("project")

        assertTrue(result)
        verify(projectRepository).existsByName("project")
    }

    @Test
    fun `test create`() {
        whenever(projectRepository.existsByName("project")).thenReturn(false)
        whenever(projectRepository.save(project)).thenReturn(project)

        val result = projectService.create(project)

        assertEquals(project, result)
        verify(projectRepository).existsByName("project")
        verify(projectRepository).save(project)
    }

    @Test
    fun `test create when project name is already exists`() {
        whenever(projectRepository.existsByName("project")).thenReturn(true)

        assertThrows<EntityExistsException> {
            projectService.create(project)
        }
    }

    @Test
    fun `test update`() {
        val request = UpdateProjectRequest(
            name = "new name",
            description = "new description"
        )
        val existedProject = project

        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))
        whenever(projectRepository.save(existedProject)).thenReturn(existedProject)

        val result = projectService.update(request, 1L)

        assertEquals(existedProject, result)
    }

    @Test
    fun `test update when project not found`() {
        val request = UpdateProjectRequest(
            name = "new name",
            description = "new description"
        )

        whenever(projectRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.update(request, 1L)
        }
    }

    @Test
    fun `test deleteById`() {
        projectService.deleteById(1L)

        verify(projectRepository).deleteById(1L)
    }

    @Test
    fun `test assignedUserToProject`() {
        val user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(projectRepository.save(project)).thenReturn(project)

        projectService.assignedUserToProject(1L, "username")

        verify(projectRepository).findById(1L)
        verify(userRepository).findByUsername("username")
    }

    @Test
    fun `test assignedUserToProject when project not found`() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.assignedUserToProject(1L, "username")
        }
    }

    @Test
    fun `test assignedUserToProject when user not found`() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.assignedUserToProject(1L, "username")
        }
    }

    @Test
    fun `test unassignedUserToProject`() {
        val user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )

        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(projectRepository.save(project)).thenReturn(project)

        projectService.assignedUserToProject(1L, "username")

        verify(projectRepository).findById(1L)
        verify(userRepository).findByUsername("username")
    }

    @Test
    fun `test unassignedUserToProject when project not found`() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.assignedUserToProject(1L, "username")
        }
    }

    @Test
    fun `test unassignedUserToProject when user not found`() {
        whenever(projectRepository.findById(1L)).thenReturn(Optional.of(project))
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            projectService.assignedUserToProject(1L, "username")
        }
    }

    @Test
    fun `test getProjectsByUser`() {
        val user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))
        whenever(projectRepository.findAllByUsers_Id(1L)).thenReturn(listOf(project))

        val result = projectService.getProjectsByUser("username")

        assertEquals(1, result.size)
        verify(userRepository).findByUsername("username")
        verify(projectRepository).findAllByUsers_Id(1L)
    }
}