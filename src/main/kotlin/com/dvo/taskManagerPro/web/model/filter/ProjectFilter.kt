package com.dvo.taskManagerPro.web.model.filter

import com.dvo.taskManagerPro.validation.ProjectFilterValid
import java.time.LocalDate

@ProjectFilterValid
data class ProjectFilter(
    val pageNumber: Int? = null,
    val pageSize: Int? = null,
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val minCreatedAt: LocalDate? = null,
    val maxCreatedAt: LocalDate? = null,
    val username: String? = null,
    val taskId: Long? = null
)
