package com.dvo.taskManagerPro.mapper

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import com.dvo.taskManagerPro.web.model.request.UpsertTaskRequest
import com.dvo.taskManagerPro.web.model.response.TaskResponse
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface TaskMapper {
    @Mapping(source = "assignedTo.username", target = "username")
    @Mapping(source = "project.id", target = "projectId")
    fun taskToResponse(task: Task): TaskResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "comments", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "labels", expression = "java(new java.util.ArrayList<>())")
    @Mapping(source = "request.description", target = "description")
    fun requestToTask(request: UpsertTaskRequest, project: Project): Task

    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateRequestToTask(request: UpdateTaskRequest, @MappingTarget task: Task)
}