package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest

interface ProjectService {
    fun findAll(): List<Project>
    fun findAllByFilter(filter: ProjectFilter): List<Project>
    fun findById(id: Long): Project
    fun existsByName(name: String): Boolean
    fun create(project: Project): Project
    fun update(project: UpdateProjectRequest, id: Long): Project
    fun deleteById(id: Long)
    fun assignedUserToProject(projectId: Long, username: String)
    fun unassignedUserFromProject(projectId: Long, username: String)
    fun getProjectsByUser(username: String): List<Project>
}