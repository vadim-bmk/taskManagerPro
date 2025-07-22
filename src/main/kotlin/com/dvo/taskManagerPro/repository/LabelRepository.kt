package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Label
import org.springframework.data.jpa.repository.JpaRepository

interface LabelRepository: JpaRepository<Label, Long> {
    fun existsByName(name: String): Boolean
    fun findAllByTasks_Id(taskId: Long): List<Label>
}