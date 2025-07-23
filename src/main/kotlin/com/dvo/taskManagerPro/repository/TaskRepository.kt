package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.Optional

interface TaskRepository : JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    fun findByAssignedToId(userId: Long): List<Task>
    fun existsByTitle(title: String): Boolean
}