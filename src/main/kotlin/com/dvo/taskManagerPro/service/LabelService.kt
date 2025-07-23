package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LabelService {
    fun findAll(pageable: Pageable): Page<Label>
    fun findById(id: Long): Label
    fun create(label: Label): Label
    fun update(label: UpsertLabelRequest, id: Long): Label
    fun deleteById(id: Long)
    fun findLabelsByTaskId(taskId: Long): List<Label>
}