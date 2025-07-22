package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Long> {
    fun existsByName(name: String): Boolean
    fun findAllByUsers_Id(userId: Long): List<Project>
}