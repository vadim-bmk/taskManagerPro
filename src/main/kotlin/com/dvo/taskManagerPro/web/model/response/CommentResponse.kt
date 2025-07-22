package com.dvo.taskManagerPro.web.model.response

data class CommentResponse(
    val id: Long,
    val text: String,
    val username: String,
    val taskId: Long
)
