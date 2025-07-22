package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Comment
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest

interface CommentService {
    fun findAll(): List<Comment>
    fun findById(id: Long): Comment
    fun create(comment: UpsertCommentRequest): Comment
    fun update(comment: UpdateCommentRequest, id: Long): Comment
    fun deleteById(id: Long)

}