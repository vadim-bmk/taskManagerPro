package com.dvo.taskManagerPro.web.model.request

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Данные для обновления задачи")
data class UpdateTaskRequest(
    @Schema(description = "Название задачи", example = "Задача 1", required = false)
    val title: String? = null,
    @Schema(description = "Описание задачи", example = "Описание задачи 1", required = false)
    val description: String? = null,
    @Schema(description = "Дата окончания задачи", example = "2025-07-01", required = false)
    val dueDate: LocalDate? = null,
    @Schema(description = "Статус задачи", example = "TODO", required = false)
    val status: TaskStatus? = null,
    @Schema(description = "Приоритет задачи", example = "HIGH", required = false)
    val priority: TaskPriority? = null
)
