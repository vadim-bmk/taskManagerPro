package com.dvo.taskManagerPro.mapper

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.web.model.request.UpdateProjectRequest
import com.dvo.taskManagerPro.web.model.request.UpsertProjectRequest
import com.dvo.taskManagerPro.web.model.response.ProjectResponse
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
abstract class ProjectMapper {

    @Mapping(source = "users", target = "usersUsername", qualifiedByName = ["mapUsersToUsernames"])
    @Mapping(source = "tasks", target = "tasksId", qualifiedByName = ["mapTasksToIds"])
    abstract fun projectToResponse(project: Project): ProjectResponse

    @Named("mapUsersToUsernames")
    fun mapUsersToUsernames(users: List<com.dvo.taskManagerPro.entity.User>): List<String> {
        return users.map { mapUserToUsername(it) }
    }

    @Named("mapTasksToIds")
    fun mapTasksToIds(tasks: List<com.dvo.taskManagerPro.entity.Task>): List<Long> {
        return tasks.map { mapTaskToId(it) }
    }

    @Named("mapUserToUsername")
    fun mapUserToUsername(user: com.dvo.taskManagerPro.entity.User): String = user.username

    @Named("mapTaskToId")
    fun mapTaskToId(task: com.dvo.taskManagerPro.entity.Task): Long = task.id

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "tasks", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    abstract fun requestToProject(request: UpsertProjectRequest): Project

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun updateRequestToProject(request: UpdateProjectRequest, @MappingTarget project: Project)
}