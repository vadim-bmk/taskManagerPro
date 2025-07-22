package com.dvo.taskManagerPro.web.model.request

import jakarta.validation.constraints.NotBlank

data class UpsertLabelRequest(
    @field:NotBlank(message = "Поле name (название) должно быть заполнено")
    val name: String
)
