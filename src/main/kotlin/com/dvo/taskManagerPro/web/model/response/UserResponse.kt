package com.dvo.taskManagerPro.web.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Данные пользователя на вывод")
data class UserResponse(
    @Schema(description = "Имя пользователя")
    val username: String,
    @Schema(description = "Email пользователя")
    val email: String,
    @Schema(description = "Роль пользователя")
    val role: String
)
