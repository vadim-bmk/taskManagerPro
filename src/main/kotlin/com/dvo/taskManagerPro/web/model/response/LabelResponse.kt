package com.dvo.taskManagerPro.web.model.response

data class LabelResponse(
    val id: Long,
    val name: String,
    val tasks: List<TaskResponse>?
)
