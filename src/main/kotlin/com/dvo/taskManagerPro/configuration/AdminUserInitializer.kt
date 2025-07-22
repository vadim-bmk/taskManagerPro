package com.dvo.taskManagerPro.configuration

import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminUserInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (userRepository.findByUsername("admin").isEmpty) {
            val admin = User(
                username = "admin",
                email = "admin@mail.ru",
                password = passwordEncoder.encode("12345"),
                roleType = RoleType.ROLE_ADMIN,
                tasks = mutableListOf(),
                projects = mutableListOf()
            )

            userRepository.save(admin)
        }
    }
}