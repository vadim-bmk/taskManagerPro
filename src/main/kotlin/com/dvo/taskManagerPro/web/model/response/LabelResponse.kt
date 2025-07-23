package com.dvo.taskManagerPro.web.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Данные метки на вывод")
data class LabelResponse(
    @Schema(description = "Идентификатор метки")
    val id: Long,

    @Schema(description = "Название метки")
    val name: String,

    @Schema(description = "Задачи, которые привязаны к метке")
    val tasks: List<TaskResponse>?
)
