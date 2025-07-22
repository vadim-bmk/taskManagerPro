package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest

interface LabelService {
    fun findAll(): List<Label>
    fun findById(id: Long): Label
    fun create(label: Label): Label
    fun update(label: UpsertLabelRequest, id: Long): Label
    fun deleteById(id: Long)
    fun findLabelsByTaskId(taskId: Long): List<Label>
}