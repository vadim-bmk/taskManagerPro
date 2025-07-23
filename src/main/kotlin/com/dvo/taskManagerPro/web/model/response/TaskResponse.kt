package com.dvo.taskManagerPro.web.model.response

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "Данные задачи на вывод")
data class TaskResponse(
    @Schema(description = "Идентификатор задачи")
    val id: Long,
    @Schema(description = "Название задачи")
    val title: String,
    @Schema(description = "Описание задачи")
    val description: String?,
    @Schema(description = "Дата создания задачи")
    val createdAt: LocalDateTime,
    @Schema(description = "Дата окончания задачи")
    val dueDate: LocalDate,
    @Schema(description = "Статус задачи")
    val status: TaskStatus,
    @Schema(description = "Приоритет задачи")
    val priority: TaskPriority,
    @Schema(description = "Имя пользователя, которому назначена задача")
    val username: String?,
    @Schema(description = "Идентификатор проекта, к которому относится задача")
    val projectId: Long?
)
