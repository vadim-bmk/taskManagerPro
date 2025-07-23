package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.LabelMapper
import com.dvo.taskManagerPro.service.LabelService
import com.dvo.taskManagerPro.swagger.StandardErrorResponses
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import com.dvo.taskManagerPro.web.model.response.LabelResponse
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "Label Management", description = "Operations for managing labels")
@RestController
@RequestMapping("/api/labels")
class LabelController(
    private val labelService: LabelService,
    private val labelMapper: LabelMapper
) {
    @Operation(summary = "Get all labels")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Запрос выполнен"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findAll(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Pagination parameters",
            required = true
        )
        @RequestBody @Valid paginationRequest: PaginationRequest
    ): ResponseEntity<ModelListResponse<LabelResponse>> {
        val labels = labelService.findAll(paginationRequest.pageRequest()).toList()
        val response = ModelListResponse(
            totalCount = labels.size.toLong(),
            data = labels.map(labelMapper::labelToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Get label by id")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Запрос выполнен",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LabelResponse::class))]
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findById(
        @Parameter(description = "ID of the label to retrieve", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<LabelResponse> {
        val label = labelService.findById(id)

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @Operation(summary = "Create a new label")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Запрос выполнен",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LabelResponse::class))]
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Label creation request",
            required = true
        )
        @RequestBody request: UpsertLabelRequest
    ): ResponseEntity<LabelResponse> {
        val label = labelService.create(labelMapper.requestToLabel(request))

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @Operation(summary = "Update a label")
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Запрос выполнен",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LabelResponse::class))]
    )
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun update(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Label creation request",
            required = true
        )
        @RequestBody request: UpsertLabelRequest,
        @Parameter(description = "ID of the label to update", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<LabelResponse> {
        val label = labelService.update(request, id)

        return ResponseEntity.ok(labelMapper.labelToResponse(label))
    }

    @Operation(
        summary = "Delete a label",
        description = "Deletes a label by its ID. Accessible only to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "204",
        description = "Запрос выполнен"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun deleteById(@PathVariable id: Long): ResponseEntity<LabelResponse> {
        labelService.deleteById(id)

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Get labels by task ID",
        description = "Returns a list of labels associated with a specific task."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Запрос выполнен",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LabelResponse::class))]
    )
    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    fun findLabelsByTaskId(
        @Parameter(description = "ID of the task to retrieve labels for", example = "1")
        @PathVariable taskId: Long
    ): ResponseEntity<ModelListResponse<LabelResponse>> {
        val labels = labelService.findLabelsByTaskId(taskId)
        val response = ModelListResponse(
            totalCount = labels.size.toLong(),
            data = labels.map(labelMapper::labelToResponse)
        )

        return ResponseEntity.ok(response)
    }
}