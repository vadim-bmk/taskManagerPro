package com.dvo.taskManagerPro.web.model.request

import com.dvo.taskManagerPro.entity.TaskPriority
import com.dvo.taskManagerPro.entity.TaskStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Schema(description = "Данные для создания задачи")
data class UpsertTaskRequest(
    @Schema(description = "Название задачи", example = "Задача 1")
    @field:NotBlank(message = "Поле title (название) должно быть заполнено")
    val title: String,

    @Schema(description = "Описание задачи", example = "Описание задачи 1")
    val description: String? = null,

    @Schema(description = "Дата окончания задачи", example = "2025-07-01")
    @field:NotNull(message = "Поле dueDate (срок) должно быть заполнено")
    val dueDate: LocalDate,

    @Schema(description = "Статус задачи", example = "TODO")
    @field:NotNull(message = "Поле status (статус) должно быть заполнено")
    val status: TaskStatus,

    @Schema(description = "Приоритет задачи", example = "HIGH")
    @field:NotNull(message = "Поле priority (приоритет) должно быть заполнено")
    val priority: TaskPriority
)
