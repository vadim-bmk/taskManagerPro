package com.dvo.taskManagerPro.mapper

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import com.dvo.taskManagerPro.web.model.response.LabelResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = [TaskMapper::class])
interface LabelMapper {
    fun labelToResponse(label: Label): LabelResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    fun requestToLabel(request: UpsertLabelRequest): Label

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    fun updateRequestToLabel(request: UpsertLabelRequest, @MappingTarget label: Label)
}