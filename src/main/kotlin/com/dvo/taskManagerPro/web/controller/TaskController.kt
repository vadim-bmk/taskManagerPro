package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.TaskMapper
import com.dvo.taskManagerPro.service.ProjectService
import com.dvo.taskManagerPro.service.TaskService
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import com.dvo.taskManagerPro.web.model.request.UpsertTaskRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.TaskResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService,
    private val taskMapper: TaskMapper,
    private val projectService: ProjectService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): ResponseEntity<ModelListResponse<TaskResponse>> {
        val tasks = taskService.findAll()
        val response = ModelListResponse(
            totalCount = tasks.size.toLong(),
            data = tasks.map(taskMapper::taskToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        val task = taskService.findById(id)

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody @Valid request: UpsertTaskRequest,
        @RequestParam projectId: Long
    ): ResponseEntity<TaskResponse> {
        val project = projectService.findById(projectId)
        val task = taskService.create(taskMapper.requestToTask(request, project))

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @RequestBody @Valid request: UpdateTaskRequest,
        @PathVariable id: Long
    ): ResponseEntity<TaskResponse> {
        val task = taskService.update(request, id)

        return ResponseEntity.ok(taskMapper.taskToResponse(task))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        taskService.deleteById(id)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/id/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    fun assignTaskToUser(
        @PathVariable id: Long,
        @RequestParam userId: Long
    ): ResponseEntity<Unit> {
        taskService.assignTaskToUser(userId, id)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/id/{id}/delUser")
    @ResponseStatus(HttpStatus.OK)
    fun unassignTaskFromUser(@PathVariable id: Long): ResponseEntity<Unit> {
        taskService.unassignTaskFromUser(id)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/id/{id}/label")
    @ResponseStatus(HttpStatus.OK)
    fun assignLabelToTask(
        @PathVariable id: Long,
        @RequestParam labelId: Long
    ): ResponseEntity<Unit> {
        taskService.assignLabelToTask(id, labelId)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/id/{id}/delLabel")
    @ResponseStatus(HttpStatus.OK)
    fun unassignLabelFromTask(
        @PathVariable id: Long,
        @RequestParam labelId: Long
    ): ResponseEntity<Unit> {
        taskService.unassignLabelFromTask(id, labelId)

        return ResponseEntity.ok().build()
    }
}