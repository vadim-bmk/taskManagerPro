package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Данные для создания пользователя")
data class UpsertUserRequest(
    @Schema(description = "Имя пользователя", example = "user1")
    @field:NotBlank(message = "Поле логин (username) должно быть заполнено и уникально")
    val username: String,

    @Schema(description = "Email пользователя", example = "user1@gmail.com")
    @field:NotBlank(message = "Поле email должно быть заполнено и уникально")
    @field:Email(message = "Поле email должно быть корректным email")
    val email: String,

    @Schema(description = "Пароль пользователя", example = "12345")
    @field:NotBlank(message = "Поле пароль должно быть заполнено")
    @field:Size(min = 5, max = 30, message = "Пароль должен быть от {min} до {max} символов")
    val password: String
)
