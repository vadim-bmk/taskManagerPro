package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.ProjectMapper
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import com.dvo.taskManagerPro.web.model.request.UpsertProjectRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.ProjectResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService,
    private val projectMapper: ProjectMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findAllByFilter(@Valid filter: ProjectFilter): ResponseEntity<ModelListResponse<ProjectResponse>> {
        val projects = projectService.findAllByFilter(filter)
        val response = ModelListResponse(
            totalCount = projects.size.toLong(),
            data = projects.map(projectMapper::projectToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findById(@PathVariable id: Long): ResponseEntity<ProjectResponse> {
        val project = projectService.findById(id)

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun create(@RequestBody request: UpsertProjectRequest): ResponseEntity<ProjectResponse> {
        val project = projectService.create(projectMapper.requestToProject(request))

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun update(
        @RequestBody request: UpdateProjectRequest,
        @PathVariable id: Long
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.update(request, id)

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        projectService.deleteById(id)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/add/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun assignedUserToProject(
        @PathVariable id: Long,
        @PathVariable username: String
    ): ResponseEntity<Unit> {
        projectService.assignedUserToProject(id, username)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}/delete/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun unassignedUserFromProject(
        @PathVariable id: Long,
        @PathVariable username: String
    ): ResponseEntity<Unit> {
        projectService.unassignedUserFromProject(id, username)

        return ResponseEntity.ok().build()
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun getProjectsByUser(@PathVariable username: String): ResponseEntity<ModelListResponse<ProjectResponse>> {
        val projects = projectService.getProjectsByUser(username)
        val response = ModelListResponse(
            totalCount = projects.size.toLong(),
            data = projects.map(projectMapper::projectToResponse)
        )

        return ResponseEntity.ok(response)
    }
}