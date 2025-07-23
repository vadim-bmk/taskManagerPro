package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.ProjectMapper
import com.dvo.taskManagerPro.repository.ProjectRepository
import com.dvo.taskManagerPro.repository.ProjectSpecification
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val projectMapper: ProjectMapper
) : ProjectService {
    private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)

    override fun findAll(): List<Project> {
        log.info("Call findAll in ProjectServiceImpl")

        return projectRepository.findAll()
    }

    override fun findAllByFilter(filter: ProjectFilter): List<Project> {
        log.info("Call findAllByFilter in ProjectServiceImpl with filter: {}", filter)

        val spec = ProjectSpecification.withFilter(filter)

        return projectRepository.findAll(
            spec,
            PageRequest.of(filter.pageNumber!!, filter.pageSize!!)
        ).content
    }

    override fun findById(id: Long): Project {
        log.info("Call findById in ProjectServiceImpl with ID: {}", id)

        return projectRepository.findById(id)
            .orElseThrow {
                throw EntityNotFoundException("Project with ID: $id not found")
            }
    }

    override fun existsByName(name: String): Boolean {
        log.info("Call existsByName in ProjectServiceImpl with name: {}", name)

        return projectRepository.existsByName(name)
    }

    @Transactional
    override fun create(project: Project): Project {
        log.info("Call create in ProjectServiceImpl with project: {}", project)

        if (projectRepository.existsByName(project.name)) {
            throw EntityExistsException("Project with name: ${project.name} is already exists")
        }

        return projectRepository.save(project)
    }

    @Transactional
    override fun update(project: UpdateProjectRequest, id: Long): Project {
        log.info("Call update in ProjectServiceImpl with ID: {} and project: {}", id, project)

        val existedProject = projectRepository.findById(id)
            .orElseThrow {
                throw EntityNotFoundException("Project with ID: $id not found")
            }

        if (project.name != null && project.name != existedProject.name && projectRepository.existsByName(project.name)) {
            throw EntityExistsException("Project with name: ${project.name} is already exists")
        }

        projectMapper.updateRequestToProject(project, existedProject)

        return projectRepository.save(existedProject)
    }

    @Transactional
    override fun deleteById(id: Long) {
        log.info("Call deleteById in ProjectServiceImpl with ID: {}", id)

        return projectRepository.deleteById(id)
    }

    @Transactional
    override fun assignedUserToProject(projectId: Long, username: String) {
        log.info(
            "Call assignedUserToProject in ProjectServiceImpl with projectId: {} and username: {}",
            projectId,
            username
        )

        val project = projectRepository.findById(projectId)
            .orElseThrow {
                throw EntityNotFoundException("Project with ID: $projectId not found")
            }

        val user = userRepository.findByUsername(username)
            .orElseThrow {
                throw EntityNotFoundException("User with username: $username not found")
            }

        if (!project.users.contains(user)) {
            project.users.add(user)
            projectRepository.save(project)
        }
    }

    @Transactional
    override fun unassignedUserFromProject(projectId: Long, username: String) {
        log.info(
            "Call unassignedUserFromProject in ProjectServiceImpl with projectId: {} and username: {}",
            projectId,
            username
        )

        val project = projectRepository.findById(projectId)
            .orElseThrow {
                throw EntityNotFoundException("Project with ID: $projectId not found")
            }

        val user = userRepository.findByUsername(username)
            .orElseThrow {
                throw EntityNotFoundException("User with username: $username not found")
            }

        if (project.users.contains(user)) {
            project.users.remove(user)
            projectRepository.save(project)
        }
    }

    override fun getProjectsByUser(username: String): List<Project> {
        log.info("Call getProjectsByUser in ProjectServiceImpl with username: {}", username)

        val userId = userRepository.findByUsername(username)
            .orElseThrow {
                throw EntityNotFoundException("User with username: $username not found")
            }.id

        return projectRepository.findAllByUsers_Id(userId)
    }

}