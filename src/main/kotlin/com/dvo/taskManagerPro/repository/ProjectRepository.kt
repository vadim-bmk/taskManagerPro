package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ProjectRepository : JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    fun existsByName(name: String): Boolean
    fun findAllByUsers_Id(userId: Long): List<Project>
}