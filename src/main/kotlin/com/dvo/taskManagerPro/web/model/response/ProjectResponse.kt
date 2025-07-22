package com.dvo.taskManagerPro.web.model.response

import java.time.LocalDateTime

data class ProjectResponse(
    val id: Long,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime,
    val tasksId: List<Long>,
    val usersUsername: List<String>
)
