package com.dvo.taskManagerPro.validation

import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.lang3.ObjectUtils

class TaskFilterValidValidator : ConstraintValidator<TaskFilterValid, TaskFilter> {
    override fun isValid(p0: TaskFilter, p1: ConstraintValidatorContext): Boolean {
        return !ObjectUtils.anyNull(p0.pageNumber, p0.pageSize)
    }
}