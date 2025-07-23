package com.dvo.taskManagerPro.validation

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [TaskFilterValidValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaskFilterValid(
    val message: String = "Поля для пагинации должны быть указаны!",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)
