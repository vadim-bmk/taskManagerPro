package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Данные для создания проекта")
data class UpsertProjectRequest(
    @Schema(description = "Название проекта", example = "Проект 1")
    @field:NotBlank(message = "Поле название (name) должно быть заполнено")
    val name: String,

    @Schema(description = "Описание проекта", example = "Описание проекта 1")
    val description: String? = null
)
