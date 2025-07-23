package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Данные для обновления комментария")
data class UpdateCommentRequest(
    @Schema(description = "Текст комментария", example = "Комментарий 1", required = false)
    val text: String? = null,

    @Schema(description = "Имя пользователя", example = "user1", required = false)
    val username: String? = null,

    @Schema(description = "ID задачи", example = "1", required = false)
    val taskId: Long? = null
)
