package com.dvo.taskManagerPro.web.model.filter

import com.dvo.taskManagerPro.validation.ProjectFilterValid
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Фильтр для проектов")
@ProjectFilterValid
data class ProjectFilter(
    @Schema(description = "Номер страницы", example = "0")
    val pageNumber: Int? = null,

    @Schema(description = "Количество элементов на странице", example = "10")
    val pageSize: Int? = null,

    @Schema(description = "ID проекта", example = "1")
    val id: Long? = null,

    @Schema(description = "Название проекта", example = "Проект 1")
    val name: String? = null,

    @Schema(description = "Описание проекта", example = "Описание проекта 1")
    val description: String? = null,

    @Schema(description = "Дата создания проекта от", example = "2025-07-01")
    val minCreatedAt: LocalDate? = null,

    @Schema(description = "Дата создания проекта до", example = "2025-08-01")
    val maxCreatedAt: LocalDate? = null,

    @Schema(description = "Имя пользователя", example = "user1")
    val username: String? = null,

    @Schema(description = "ID задачи", example = "1")
    val taskId: Long? = null
)
