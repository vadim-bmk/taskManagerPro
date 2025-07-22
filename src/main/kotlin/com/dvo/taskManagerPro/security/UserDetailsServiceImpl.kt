package com.dvo.taskManagerPro.security

import com.dvo.taskManagerPro.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val actualUsername = username ?: throw UsernameNotFoundException("Username is null")
        val user = userService.findByUsername(actualUsername)
        return AppUserPrincipal(user)
    }
}