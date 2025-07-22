package com.dvo.taskManagerPro.web.model.request

import jakarta.validation.constraints.NotBlank

data class UpsertProjectRequest(
    @field:NotBlank(message = "Поле название (name) должно быть заполнено")
    val name: String,

    val description: String? = null
)
