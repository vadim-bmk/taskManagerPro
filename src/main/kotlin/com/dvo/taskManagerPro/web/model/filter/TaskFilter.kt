package com.dvo.taskManagerPro.web.model.filter

import com.dvo.taskManagerPro.validation.TaskFilterValid
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Фильтр для задач")
@TaskFilterValid
data class TaskFilter(
    @Schema(description = "Номер страницы", example = "0")
    var pageNumber: Int? = null,
    @Schema(description = "Количество элементов на странице", example = "10")
    var pageSize: Int? = null,
    @Schema(description = "ID задачи", example = "1")
    var id: Long? = null,
    @Schema(description = "Название задачи", example = "Задача 1")
    var title: String? = null,
    @Schema(description = "Описание задачи", example = "Описание задачи 1")
    var description: String? = null,
    @Schema(description = "Дата создания задачи от", example = "2025-07-01")
    var minCreatedAt: LocalDate? = null,
    @Schema(description = "Дата создания задачи до", example = "2025-08-01")
    var maxCreatedAt: LocalDate? = null,
    @Schema(description = "Дата окончания задачи от", example = "2025-07-01")
    var minDueDate: LocalDate? = null,
    @Schema(description = "Дата окончания задачи до", example = "2025-08-01")
    var maxDueDate: LocalDate? = null,
    @Schema(description = "Статус задачи", example = "TODO")
    var status: String? = null,
    @Schema(description = "Приоритет задачи", example = "HIGH")
    var priority: String? = null,
    @Schema(description = "Имя пользователя", example = "user1")
    var username: String? = null,
    @Schema(description = "ID проекта", example = "1")
    var projectId: Long? = null,
    @Schema(description = "ID метки", example = "1")
    var labelId: Long? = null
)
