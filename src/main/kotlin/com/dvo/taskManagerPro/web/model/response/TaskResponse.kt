package com.dvo.taskManagerPro.web.model.response

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val dueDate: LocalDate,
    val status: TaskStatus,
    val priority: TaskPriority,
    val username: String?,
    val projectId: Long?
)
