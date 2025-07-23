package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.aop.CheckAccessToTask
import com.dvo.taskManagerPro.mapper.TaskMapper
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.service.TaskService
import com.dvo.taskManagerPro.swagger.StandardErrorResponses
import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import com.dvo.taskManagerPro.web.model.request.UpsertTaskRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.TaskResponse
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Task Management", description = "Operations for managing tasks")
@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService,
    private val taskMapper: TaskMapper,
    private val projectService: ProjectService
) {

    @Operation(summary = "Get all tasks", description = "Returns list of all users by filter.")
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Tasks returned successfully")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findAllByFilter(
        @ParameterObject
        @Valid filter: TaskFilter
    ): ResponseEntity<ModelListResponse<TaskResponse>> {
        val tasks = taskService.findAllByFilter(filter)
        val response = ModelListResponse(
            totalCount = tasks.size.toLong(),
            data = tasks.map(taskMapper::taskToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Get task by id", description = "Returns task by id.")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Task returned successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskResponse::class))]
    )
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findById(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<TaskResponse> {
        val task = taskService.findById(id)

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @Operation(summary = "Create task", description = "Creates new task. Only accessible to ADMIN and MANAGER roles.")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Task created successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskResponse::class))]
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task creation request",
            required = true
        )
        @RequestBody @Valid request: UpsertTaskRequest,
        @Parameter(description = "Project id", example = "1")
        @RequestParam projectId: Long
    ): ResponseEntity<TaskResponse> {
        val project = projectService.findById(projectId)
        val task = taskService.create(taskMapper.requestToTask(request, project))

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @Operation(summary = "Update task", description = "Updates task. Access limited by roles and AOP logic.")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Task updated successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskResponse::class))]
    )
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToTask
    fun update(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task updating request",
            required = true
        )
        @RequestBody @Valid request: UpdateTaskRequest,
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<TaskResponse> {
        val task = taskService.update(request, id)

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @Operation(summary = "Delete task", description = "Deletes task. Only accessible to ADMIN and MANAGER roles.")
    @StandardErrorResponses
    @ApiResponse(responseCode = "204", description = "Task deleted successfully")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun deleteById(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        taskService.deleteById(id)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Assign task to user",
        description = "Assigns task to user. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Task assigned to user successfully")
    @PutMapping("/id/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun assignTaskToUser(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Username of the user to assign task to", example = "user1")
        @RequestParam username: String
    ): ResponseEntity<Unit> {
        taskService.assignTaskToUser(username, id)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Unassign task from user",
        description = "Unassigns task from user. Access limited by roles and AOP logic."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Task unassigned from user successfully")
    @PutMapping("/id/{id}/delUser")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToTask
    fun unassignTaskFromUser(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        taskService.unassignTaskFromUser(id)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Assign label to task",
        description = "Assigns label to task. Access limited by roles and AOP logic."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Label assigned to task successfully")
    @PutMapping("/id/{id}/label")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToTask
    fun assignLabelToTask(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Label id", example = "1")
        @RequestParam labelId: Long
    ): ResponseEntity<Unit> {
        taskService.assignLabelToTask(id, labelId)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Unassign label from task",
        description = "Unassigns label from task. Access limited by roles and AOP logic."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Label unassigned from task successfully")
    @PutMapping("/id/{id}/delLabel")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToTask
    fun unassignLabelFromTask(
        @Parameter(description = "Task id", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Label id", example = "1")
        @RequestParam labelId: Long
    ): ResponseEntity<Unit> {
        taskService.unassignLabelFromTask(id, labelId)

        return ResponseEntity.ok().build()
    }
}