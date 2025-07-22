package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}