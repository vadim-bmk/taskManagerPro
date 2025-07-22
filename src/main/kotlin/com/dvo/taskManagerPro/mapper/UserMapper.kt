package com.dvo.taskManagerPro.mapper

import com.dvo.taskManagerPro.entity.RoleType
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.web.model.request.UpdateUserRequest
import com.dvo.taskManagerPro.web.model.request.UpsertUserRequest
import com.dvo.taskManagerPro.web.model.response.UserResponse
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    @Mapping(target = "role", source = "roleType")
    fun userToResponse(user: User): UserResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "projects", ignore = true)
    fun requestToUser(request: UpsertUserRequest, roleType: RoleType): User

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roleType", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "projects", ignore = true)
    fun updateRequestToUser(request: UpdateUserRequest, @MappingTarget user: User)
}