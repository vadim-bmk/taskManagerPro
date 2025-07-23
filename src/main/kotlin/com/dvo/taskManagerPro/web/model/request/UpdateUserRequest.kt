package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Данные для обновления пользователя")
data class UpdateUserRequest(
    @Schema(description = "Имя пользователя", example = "user1")
    @field:NotBlank(message = "Поле пароль (password) должно быть заполнено")
    @field:Size(min = 5, max = 30, message = "Пароль не может быть меньше {min} и больше {max}!")
    val password: String,

    @Schema(description = "Электронная почта", example = "user1@gmail.com")
    @field:NotBlank(message = "Поле электронная почта (email) должно быть заполнено")
    @field:Email
    val email: String
)
