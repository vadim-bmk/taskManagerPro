package com.dvo.taskManagerPro.web.model.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "Поле пароль (password) должно быть заполнено")
    @field:Size(min = 5, max = 30, message = "Пароль не может быть меньше {min} и больше {max}!")
    val password: String,

    @field:NotBlank(message = "Поле электронная почта (email) должно быть заполнено")
    @field:Email
    val email: String
)
