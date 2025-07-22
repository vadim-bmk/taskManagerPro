package com.dvo.taskManagerPro.web.model.response

data class ModelListResponse<T>(
    val totalCount: Long,
    val data: List<T>
)
