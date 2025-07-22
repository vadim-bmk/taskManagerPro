package com.dvo.taskManagerPro.web.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpsertCommentRequest(
    @field:NotBlank(message = "Поле text (текст) должно быть заполнено")
    val text: String,

    @field:NotBlank(message = "Поле username (имя пользователя) должно быть заполнено")
    val username: String,

    @field:NotNull(message = "Поле taskId (id задачи) должно быть заполнено")
    val taskId: Long
)
