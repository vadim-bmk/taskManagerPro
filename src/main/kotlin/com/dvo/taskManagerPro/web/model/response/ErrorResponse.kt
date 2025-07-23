package com.dvo.taskManagerPro.web.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Стандартный ответ об ошибке")
data class ErrorResponse(
    @Schema(description = "Сообщение об ошибке")
    val message: String,
    @Schema(description = "Подробности об ошибке")
    val details: Map<String, String>? = null
)
