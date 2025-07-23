package com.dvo.taskManagerPro.web.model.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Данные проекта на вывод")
data class ProjectResponse(
    @Schema(description = "Идентификатор проекта")
    val id: Long,

    @Schema(description = "Название проекта")
    val name: String,

    @Schema(description = "Описание проекта")
    val description: String,

    @Schema(description = "Дата создания проекта")
    val createdAt: LocalDateTime,

    @Schema(description = "Идентификаторы задач, к которым относится проект")
    val tasksId: List<Long>,

    @Schema(description = "Имена пользователей, к которым относится проект")
    val usersUsername: List<String>
)
