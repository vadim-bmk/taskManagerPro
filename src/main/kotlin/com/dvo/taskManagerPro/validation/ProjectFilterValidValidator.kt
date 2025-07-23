package com.dvo.taskManagerPro.validation

import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.commons.lang3.ObjectUtils

class ProjectFilterValidValidator : ConstraintValidator<ProjectFilterValid, ProjectFilter> {
    override fun isValid(p0: ProjectFilter, p1: ConstraintValidatorContext): Boolean {
        return !ObjectUtils.anyNull(p0.pageNumber, p0.pageSize)
    }
}