package com.dvo.taskManagerPro.web.model.request

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import java.time.LocalDate

data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val dueDate: LocalDate? = null,
    val status: TaskStatus? = null,
    val priority: TaskPriority? = null
)
