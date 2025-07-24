package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.UserMapper
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserServiceImplTest {
    private lateinit var userService: UserServiceImpl
    private val userRepository: UserRepository = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private val userMapper: UserMapper = mock()

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(userRepository, passwordEncoder, userMapper)

        user = User(
            id = 1,
            username = "username",
            email = "email@mail.ru",
            roleType = RoleType.ROLE_ADMIN,
            password = "12345",
            tasks = mutableListOf(),
            projects = mutableListOf()
        )
    }

    @Test
    fun `test findAll`() {
        val usersList = listOf(user)
        val page: Page<User> = PageImpl(usersList, PageRequest.of(0, 10), usersList.size.toLong())
        whenever(userRepository.findAll(eq(PageRequest.of(0, 10)))).thenReturn(page)

        val result = userService.findAll(PageRequest.of(0, 10))

        assertNotNull(result)
        assertEquals(1, result.totalElements)
        verify(userRepository).findAll(PageRequest.of(0, 10))
    }

    @Test
    fun `test findByUsername`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.of(user))

        val result = userService.findByUsername("username")

        assertEquals("username", result.username)
        verify(userRepository).findByUsername("username")
    }

    @Test
    fun `test findByUsername when user not found`() {
        whenever(userRepository.findByUsername("username")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            userService.findByUsername("username")
        }
    }

    @Test
    fun `test existsByUsername`() {
        whenever(userRepository.existsByUsername("username")).thenReturn(true)

        val result = userService.existsByUsername("username")

        assertTrue(result)
    }

    @Test
    fun `test existsByEmail`() {
        whenever(userRepository.existsByEmail("email@mail.ru")).thenReturn(true)

        val result = userService.existsByEmail("email@mail.ru")

        assertTrue(result)
    }

    @Test
    fun `test create`() {
        whenever(userRepository.existsByUsername("username")).thenReturn(false)
        whenever(userRepository.existsByEmail("email@mail.ru")).thenReturn(false)
        whenever(passwordEncoder.encode("12345")).thenReturn("encoded")
        whenever(userRepository.save(user)).thenReturn(user)

        val result = userService.create(user)

        assertEquals("username", result.username)
        verify(userRepository).existsByUsername("username")
        verify(userRepository).existsByEmail("email@mail.ru")
        verify(passwordEncoder).encode("12345")
        verify(userRepository).save(user)
    }

    @Test
    fun `test create when username is already exists`() {
        whenever(userRepository.existsByUsername("username")).thenReturn(true)

        assertThrows<EntityExistsException> {
            userService.create(user)
        }
    }

    @Test
    fun `test create when email is already exists`() {
        whenever(userRepository.existsByUsername("username")).thenReturn(false)
        whenever(userRepository.existsByEmail("email@mail.ru")).thenReturn(true)

        assertThrows<EntityExistsException> {
            userService.create(user)
        }
    }

    @Test
    fun `test update`() {
        val existedUser = user.copy(username = "user")
        val request = UpdateUserRequest(
            password = "12345",
            email = "new@mail.ru"
        )

        val updatedUser = existedUser.copy(email = request.email)

        whenever(userRepository.findByUsername("user")).thenReturn(Optional.of(existedUser))
        whenever(userRepository.existsByEmail(request.email)).thenReturn(false)
        whenever(passwordEncoder.encode("12345")).thenReturn("encoded")
        whenever(userRepository.save(any<User>())).thenReturn(updatedUser)

        val result = userService.update(request, "user")

        assertEquals("new@mail.ru", result.email)
        verify(userRepository).findByUsername("user")
        verify(userRepository).existsByEmail(request.email)
        verify(passwordEncoder).encode("12345")
        verify(userRepository).save(existedUser)
    }

    @Test
    fun `test update user when user not found`() {
        val request = UpdateUserRequest(
            password = "12345",
            email = "new@mail.ru"
        )
        whenever(userRepository.findByUsername("user")).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            userService.update(request, "user")
        }
    }

    @Test
    fun `test update user when email is already exists`() {
        val existedUser = user.copy(username = "user")
        val request = UpdateUserRequest(
            password = "12345",
            email = "new@mail.ru"
        )

        val updatedUser = existedUser.copy(email = request.email)

        whenever(userRepository.findByUsername("user")).thenReturn(Optional.of(existedUser))
        whenever(userRepository.existsByEmail(request.email)).thenReturn(true)

        assertThrows<EntityExistsException> {
            userService.update(request, "user")
        }
    }

    @Test
    fun `test deleteById`() {
        userService.deleteById(1L)

        verify(userRepository).deleteById(1L)
    }

    @Test
    fun `test deleteByUsername`() {
        userService.deleteByUsername("user")

        verify(userRepository).deleteByUsername("user")
    }
}