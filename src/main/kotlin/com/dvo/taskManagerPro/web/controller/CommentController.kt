package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.mapper.CommentMapper
import com.dvo.taskManagerPro.service.CommentService
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import com.dvo.taskManagerPro.web.model.response.CommentResponse
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService,
    private val commentMapper: CommentMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): ResponseEntity<ModelListResponse<CommentResponse>> {
        val comments = commentService.findAll()
        val response = ModelListResponse(
            totalCount = comments.size.toLong(),
            data = comments.map(commentMapper::commentToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable id: Long): ResponseEntity<CommentResponse> {
        val comment = commentService.findById(id)

        return ResponseEntity.ok(commentMapper.commentToResponse(comment))
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: UpsertCommentRequest): ResponseEntity<CommentResponse> {
        val comment = commentService.create(request)

        return ResponseEntity.ok(commentMapper.commentToResponse(comment))
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @RequestBody @Valid request: UpdateCommentRequest,
        @PathVariable id: Long
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.update(request, id)

        return ResponseEntity.ok(commentMapper.commentToResponse(comment))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        commentService.deleteById(id)

        return ResponseEntity.ok().build()
    }
}