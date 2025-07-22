package com.dvo.taskManagerPro.web.model.request

data class UpdateCommentRequest(
    val text: String? = null,
    val username: String? = null,
    val taskId: Long? = null
)
