package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.LabelMapper
import com.dvo.taskManagerPro.service.LabelService
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import com.dvo.taskManagerPro.web.model.response.LabelResponse
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
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
@RequestMapping("/api/labels")
class LabelController(
    private val labelService: LabelService,
    private val labelMapper: LabelMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findAll(@RequestBody @Valid paginationRequest: PaginationRequest): ResponseEntity<ModelListResponse<LabelResponse>> {
        val labels = labelService.findAll(paginationRequest.pageRequest()).toList()
        val response = ModelListResponse(
            totalCount = labels.size.toLong(),
            data = labels.map(labelMapper::labelToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findById(@PathVariable id: Long): ResponseEntity<LabelResponse> {
        val label = labelService.findById(id)

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun create(@RequestBody request: UpsertLabelRequest): ResponseEntity<LabelResponse> {
        val label = labelService.create(labelMapper.requestToLabel(request))

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun update(
        @RequestBody request: UpsertLabelRequest,
        @PathVariable id: Long
    ): ResponseEntity<LabelResponse> {
        val label = labelService.update(request, id)

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun deleteById(@PathVariable id: Long): ResponseEntity<LabelResponse> {
        labelService.deleteById(id)

        return ResponseEntity.ok().build()
    }

    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findLabelsByTaskId(@PathVariable taskId: Long): ResponseEntity<ModelListResponse<LabelResponse>> {
        val labels = labelService.findLabelsByTaskId(taskId)
        val response = ModelListResponse(
            totalCount = labels.size.toLong(),
            data = labels.map(labelMapper::labelToResponse)
        )

        return ResponseEntity.ok(response)
    }
}