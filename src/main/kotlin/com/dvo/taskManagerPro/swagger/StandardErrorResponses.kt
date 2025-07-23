package com.dvo.taskManagerPro.swagger

import com.dvo.taskManagerPro.web.model.response.ErrorResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "400",
            description = "Некорректный запрос",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Доступ запрещен",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Ресурс не найден",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Внутренняя ошибка сервера",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ]
)
annotation class StandardErrorResponses
