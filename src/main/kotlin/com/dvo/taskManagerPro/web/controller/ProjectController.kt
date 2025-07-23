package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.ProjectMapper
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.swagger.StandardErrorResponses
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import com.dvo.taskManagerPro.web.model.request.UpsertProjectRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.ProjectResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
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

@Tag(name = "Project Management", description = "Operations for managing projects")
@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService,
    private val projectMapper: ProjectMapper
) {
    @Operation(summary = "Get all projects")
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Список проектов")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findAllByFilter(
        @ParameterObject
        @Valid filter: ProjectFilter
    ): ResponseEntity<ModelListResponse<ProjectResponse>> {
        val projects = projectService.findAllByFilter(filter)
        val response = ModelListResponse(
            totalCount = projects.size.toLong(),
            data = projects.map(projectMapper::projectToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Get project by id")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Проект",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ProjectResponse::class))]
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findById(
        @Parameter(
            description = "Project id",
            example = "1"
        ) @PathVariable id: Long
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.findById(id)

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @Operation(
        summary = "Create project",
        description = "Creates new projects. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Project created",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ProjectResponse::class))]
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Project creation request",
            required = true
        )
        @RequestBody request: UpsertProjectRequest
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.create(projectMapper.requestToProject(request))

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @Operation(
        summary = "Update project",
        description = "Updates existing projects. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Project updated",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ProjectResponse::class))]
    )
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun update(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Project updating request",
            required = true
        )
        @RequestBody request: UpdateProjectRequest,
        @Parameter(description = "Project id", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.update(request, id)

        return ResponseEntity.ok(projectMapper.projectToResponse(project))
    }

    @Operation(
        summary = "Delete project",
        description = "Deletes existing projects. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "204", description = "Project deleted")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun delete(@Parameter(description = "Project id", example = "1") @PathVariable id: Long): ResponseEntity<Unit> {
        projectService.deleteById(id)

        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Assign user to project",
        description = "Assigns user to project. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "User assigned to project")
    @PutMapping("/{id}/add/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun assignedUserToProject(
        @Parameter(description = "Project id", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Username", example = "user")
        @PathVariable username: String
    ): ResponseEntity<Unit> {
        projectService.assignedUserToProject(id, username)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Unassign user from project",
        description = "Unassigns user from project. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "User unassigned from project")
    @PutMapping("/{id}/delete/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun unassignedUserFromProject(
        @Parameter(description = "Project id", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Username", example = "user")
        @PathVariable username: String
    ): ResponseEntity<Unit> {
        projectService.unassignedUserFromProject(id, username)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Get projects by user",
        description = "Gets projects by user. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Projects",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ModelListResponse::class))]
    )
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