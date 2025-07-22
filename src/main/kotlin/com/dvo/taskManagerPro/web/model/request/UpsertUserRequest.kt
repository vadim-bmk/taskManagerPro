package com.dvo.taskManagerPro.web.model.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpsertUserRequest(
    @field:NotBlank(message = "Поле логин (username) должно быть заполнено и уникально")
    val username: String,

    @field:NotBlank(message = "Поле email должно быть заполнено и уникально")
    @field:Email(message = "Поле email должно быть корректным email")
    val email: String,

    @field:NotBlank(message = "Поле пароль должно быть заполнено")
    @field:Size(min = 5, max = 30, message = "Пароль должен быть от {min} до {max} символов")
    val password: String
)
