package com.dvo.taskManagerPro.aop

import com.dvo.taskManagerPro.exception.AccessDeniedException
import com.dvo.taskManagerPro.security.AppUserPrincipal
import com.dvo.taskManagerPro.service.CommentService
import com.dvo.taskManagerPro.service.TaskService
import com.dvo.taskManagerPro.service.UserService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class AccessAspect(
    private val userService: UserService,
    private val commentService: CommentService,
    private val taskService: TaskService
) {
    @Before("@annotation(CheckAccessToUser)")
    fun checkUserAccess(joinPoint: JoinPoint) {
        val principal = SecurityContextHolder.getContext().authentication.principal as AppUserPrincipal
        val currentUser = principal.username
        val role = principal.authorities.first().authority

        val args = joinPoint.args
        val username = args.find { it is String } as? String ?: throw IllegalArgumentException("Invalid argument type")

        if (role == "ROLE_ADMIN" || role == "ROLE_MANAGER") return

        if (role == "ROLE_EMPLOYEE" && currentUser != username) {
            throw AccessDeniedException("EMPLOYEE can only access their own data")
        }
    }

    @Before("@annotation(CheckAccessToComment)")
    fun checkCommentAccess(joinPoint: JoinPoint) {
        val principal = SecurityContextHolder.getContext().authentication.principal as AppUserPrincipal
        val currentUser = principal.username
        val role = principal.authorities.first().authority

        val args = joinPoint.args
        val id = args.find { it is Long } as? Long ?: throw IllegalArgumentException("Invalid argument type")

        if (role == "ROLE_ADMIN" || role == "ROLE_MANAGER") return

        if (role == "ROLE_EMPLOYEE" && currentUser != commentService.findById(id).author.username) {
            throw AccessDeniedException("EMPLOYEE can only access their own comments")
        }
    }

    @Before("@annotation(CheckAccessToTask)")
    fun checkTaskAccess(joinPoint: JoinPoint) {
        val principal = SecurityContextHolder.getContext().authentication.principal as AppUserPrincipal
        val currentUser = principal.username
        val role = principal.authorities.first().authority

        val args = joinPoint.args
        val id = args.find { it is Long } as? Long ?: throw IllegalArgumentException("Invalid argument type")

        if (role == "ROLE_ADMIN" || role == "ROLE_MANAGER") return

        val taskAssignedTo = taskService.findById(id).assignedTo

        if (role == "ROLE_EMPLOYEE" && taskAssignedTo != null && currentUser != taskAssignedTo.username || taskAssignedTo == null) {
            throw AccessDeniedException("EMPLOYEE can only access their own tasks")
        }

    }
}