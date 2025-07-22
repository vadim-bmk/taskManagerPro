package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.UserMapper
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    @Lazy private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper

) : UserService {
    private final val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun findAll(): List<User> {
        log.info("Call findAll in UserServiceImpl")

        return userRepository.findAll()
    }

    override fun findByUsername(username: String): User {
        log.info("Call findByUsername in UserServiceImpl with username: {}", username)

        return userRepository.findByUsername(username)
            .orElseThrow {
                EntityNotFoundException("User with username $username not found")
            }
    }

    override fun existsByUsername(username: String): Boolean {
        log.info("Call existsByUsername in UserServiceImpl with username: {}", username)

        return userRepository.existsByUsername(username)
    }

    override fun existsByEmail(email: String): Boolean {
        log.info("Call existsByEmail in UserServiceImpl with email: {}", email)

        return userRepository.existsByEmail(email)
    }

    @Transactional
    override fun create(user: User): User {
        log.info(
            "Call create in UserServiceImpl with username: {}, email: {} and roleType: {}",
            user.username,
            user.email,
            user.roleType
        )

        if (userRepository.existsByUsername(user.username)) {
            throw EntityExistsException("User with username: ${user.username} is already exists")
        }

        if (userRepository.existsByEmail(user.email)) {
            throw EntityExistsException("User with email: ${user.email} is already exists")
        }

        user.password = passwordEncoder.encode(user.password)

        return userRepository.save(user)
    }

    @Transactional
    override fun update(user: UpdateUserRequest, username: String): User {
        log.info("Call update in UserServiceImpl with username: {} and user: {}", username, user)

        val existedUser = userRepository.findByUsername(username)
            .orElseThrow {
                EntityNotFoundException("User with username $username not found")
            }

        if (userRepository.existsByEmail(user.email) && user.email != existedUser.email) {
            throw EntityExistsException("User with email: ${user.email} is already exists")
        }

        userMapper.updateRequestToUser(user, existedUser)
        existedUser.password = passwordEncoder.encode(user.password)

        return userRepository.save(existedUser)
    }

    @Transactional
    override fun deleteById(id: Long) {
        log.info("Call deleteById in UserServiceImpl with ID: {}", id)

        userRepository.deleteById(id)
    }

    @Transactional
    override fun deleteByUsername(username: String) {
        log.info("Call deleteById in UserServiceImpl with username: {}", username)

        userRepository.deleteByUsername(username)
    }
}