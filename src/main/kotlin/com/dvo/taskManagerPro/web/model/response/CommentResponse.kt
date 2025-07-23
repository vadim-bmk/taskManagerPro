package com.dvo.taskManagerPro.web.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Данные комментария на вывод")
data class CommentResponse(
    @Schema(description = "Идентификатор комментария")
    val id: Long,

    @Schema(description = "Текст комментария")
    val text: String,

    @Schema(description = "Имя пользователя, который оставил комментарий")
    val username: String,

    @Schema(description = "Идентификатор задачи, к которой относится комментарий")
    val taskId: Long
)
