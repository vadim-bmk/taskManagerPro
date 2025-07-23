package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Данные для создания метки")
data class UpsertLabelRequest(
    @Schema(description = "Название метки", example = "Метка 1")
    @field:NotBlank(message = "Поле name (название) должно быть заполнено")
    val name: String
)
