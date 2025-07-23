package com.dvo.taskManagerPro.web.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.PageRequest

@Schema(description = "Стандартный запрос пагинации")
data class PaginationRequest(
    @Schema(description = "Размер страницы", example = "10")
    @field:NotNull(message = "Поле pageSize (размер страницы) должно быть заполнено")
    @field:Min(value = 1, message = "Размер страницы не может быть меньше 1!")
    val pageSize: Int = 10,

    @Schema(description = "Номер страницы", example = "0")
    @field:NotNull(message = "Поле pageNumber (номер страницы) должно быть заполнено")
    @field:Min(value = 0, message = "Номер страницы не может быть меньше 0!")
    val pageNumber: Int = 0
) {
    fun pageRequest(): PageRequest = PageRequest.of(pageNumber, pageSize)
}
