package com.dvo.taskManagerPro.web.model.request

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class UpsertTaskRequest(
    @field:NotBlank(message = "Поле title (название) должно быть заполнено")
    val title: String,
    val description: String? = null,

    @field:NotNull(message = "Поле dueDate (срок) должно быть заполнено")
    val dueDate: LocalDate,

    @field:NotNull(message = "Поле status (статус) должно быть заполнено")
    val status: TaskStatus,

    @field:NotNull(message = "Поле priority (приоритет) должно быть заполнено")
    val priority: TaskPriority
)
