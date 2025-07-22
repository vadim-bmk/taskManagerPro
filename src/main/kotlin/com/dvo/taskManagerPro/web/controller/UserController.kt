package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.mapper.UserMapper
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import com.dvo.taskManagerPro.web.model.request.UpsertUserRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.UserResponse
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
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): ResponseEntity<ModelListResponse<UserResponse>> {
        val userList = userService.findAll()
        val response = ModelListResponse(
            totalCount = userList.size.toLong(),
            data = userList.map(userMapper::userToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun findByUsername(@PathVariable username: String): ResponseEntity<UserResponse> {
        val user = userService.findByUsername(username)

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody @Valid request: UpsertUserRequest,
        @RequestParam roleType: RoleType
    ): ResponseEntity<UserResponse> {
        val user = userService.create(userMapper.requestToUser(request, roleType))

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @PutMapping("/update/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @RequestBody @Valid request: UpdateUserRequest,
        @PathVariable username: String
    ): ResponseEntity<UserResponse> {
        val user = userService.update(request, username)

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable username: String): ResponseEntity<Unit> {
        userService.deleteByUsername(username)

        return ResponseEntity.noContent().build()
    }
}