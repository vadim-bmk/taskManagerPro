package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "Данные для создания комментария")
data class UpsertCommentRequest(
    @Schema(description = "Текст комментария", example = "Комментарий 1")
    @field:NotBlank(message = "Поле text (текст) должно быть заполнено")
    val text: String,

    @Schema(description = "Имя пользователя", example = "user1")
    @field:NotBlank(message = "Поле username (имя пользователя) должно быть заполнено")
    val username: String,

    @Schema(description = "ID задачи", example = "1")
    @field:NotNull(message = "Поле taskId (id задачи) должно быть заполнено")
    val taskId: Long
)
