package com.dvo.taskManagerPro.web.model.response

data class ErrorResponse(
    val message: String,
    val details: Map<String, String>? = null
)
