package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.aop.CheckAccessToUser
import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.mapper.UserMapper
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.swagger.StandardErrorResponses
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import com.dvo.taskManagerPro.web.model.request.UpsertUserRequest
import com.dvo.taskManagerPro.web.model.response.ModelListResponse
import com.dvo.taskManagerPro.web.model.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User Management", description = "Operations for managing users")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    @Operation(
        summary = "Get all users",
        description = "Returns paginated list of all users. Only accessible to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(responseCode = "200", description = "Users returned successfully")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun findAll(
        @RequestBody @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Pagination parameters",
            required = true
        )
        paginationRequest: PaginationRequest
    ): ResponseEntity<ModelListResponse<UserResponse>> {
        val userList = userService.findAll(paginationRequest.pageRequest()).toList()
        val response = ModelListResponse(
            totalCount = userList.size.toLong(),
            data = userList.map(userMapper::userToResponse)
        )

        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get user by username",
        description = "Returns a user by username. Access limited by roles and AOP logic."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "Users returned successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
    )
    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToUser
    fun findByUsername(
        @Parameter(description = "Username of the user to retrieve", example = "user1")
        @PathVariable username: String
    ): ResponseEntity<UserResponse> {
        val user = userService.findByUsername(username)

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with provided role. Accessible without restriction."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "User created successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User creation request",
            required = true
        )
        @RequestBody @Valid request: UpsertUserRequest,
        @Parameter(description = "Role of the user to create", example = "ROLE_EMPLOYEE")
        @RequestParam roleType: RoleType
    ): ResponseEntity<UserResponse> {
        val user = userService.create(userMapper.requestToUser(request, roleType))

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @Operation(
        summary = "Update a user",
        description = "Updates a user with provided information. Access limited by roles and AOP logic."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "200",
        description = "User updated successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
    )
    @PutMapping("/update/{username}")
    @ResponseStatus(HttpStatus.OK)
    @CheckAccessToUser
    fun update(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User update request",
            required = true
        )
        @RequestBody @Valid request: UpdateUserRequest,
        @Parameter(description = "Username of the user to update", example = "user1")
        @PathVariable username: String
    ): ResponseEntity<UserResponse> {
        val user = userService.update(request, username)

        return ResponseEntity.ok(userMapper.userToResponse(user))
    }

    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by username. Accessible only to ADMIN and MANAGER roles."
    )
    @StandardErrorResponses
    @ApiResponse(
        responseCode = "204",
        description = "User deleted successfully"
    )
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    fun delete(
        @Parameter(description = "Username of the user to delete", example = "user1")
        @PathVariable username: String
    ): ResponseEntity<Unit> {
        userService.deleteByUsername(username)

        return ResponseEntity.noContent().build()
    }
}