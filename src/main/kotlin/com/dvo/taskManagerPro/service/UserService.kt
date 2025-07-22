package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest

interface UserService {
    fun findAll(): List<User>
    fun findByUsername(username: String): User
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun create(user: User): User
    fun update(user: UpdateUserRequest, username: String): User
    fun deleteById(id: Long)
    fun deleteByUsername(username: String)
}