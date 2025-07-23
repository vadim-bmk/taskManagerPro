package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Данные для обновления проекта")
data class UpdateProjectRequest(
    @Schema(description = "Название проекта", example = "Проект 1", required = false)
    val name: String? = null,

    @Schema(description = "Описание проекта", example = "Описание проекта 1", required = false)
    val description: String? = null
)
