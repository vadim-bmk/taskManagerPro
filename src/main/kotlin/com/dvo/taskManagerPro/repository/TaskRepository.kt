package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByAssignedToId(userId: Long): List<Task>
    fun existsByTitle(title: String): Boolean
}