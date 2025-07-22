package com.dvo.taskManagerPro.mapper

import com.dvo.taskManagerPro.entity.Comment
import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import com.dvo.taskManagerPro.web.model.response.CommentResponse
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "author.username", target = "username")
    fun commentToResponse(comment: Comment): CommentResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "author")
    @Mapping(source = "task", target = "task")
    fun requestToComment(
        request: UpsertCommentRequest,
        user: User,
        task: Task
    ): Comment


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "author")
    @Mapping(source = "task", target = "task")
    fun updateRequestToComment(
        request: UpdateCommentRequest,
        user: User?,
        task: Task?,
        @MappingTarget comment: Comment
    )
}