package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Comment
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentService {
    fun findAll(pageable: Pageable): Page<Comment>
    fun findById(id: Long): Comment
    fun create(comment: UpsertCommentRequest): Comment
    fun update(comment: UpdateCommentRequest, id: Long): Comment
    fun deleteById(id: Long)

}