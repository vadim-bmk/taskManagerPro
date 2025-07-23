package com.dvo.taskManagerPro.web.model.filter

import com.dvo.taskManagerPro.validation.TaskFilterValid
import java.time.LocalDate

@TaskFilterValid
data class TaskFilter(
    var pageNumber: Int? = null,
    var pageSize: Int? = null,
    var id: Long? = null,
    var title: String? = null,
    var description: String? = null,
    var minCreatedAt: LocalDate? = null,
    var maxCreatedAt: LocalDate? = null,
    var minDueDate: LocalDate? = null,
    var maxDueDate: LocalDate? = null,
    var status: String? = null,
    var priority: String? = null,
    var username: String? = null,
    var projectId: Long? = null,
    var labelId: Long? = null
)
