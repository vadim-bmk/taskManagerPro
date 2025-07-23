package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.web.model.filter.ProjectFilter
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime

object ProjectSpecification {
    fun withFilter(filter: ProjectFilter): Specification<Project> {
        return Specification.where(byId(filter.id))
            .and(byName(filter.name))
            .and(byDescription(filter.description))
            .and(byCreatedAt(filter.minCreatedAt, filter.maxCreatedAt))
            .and(byUsername(filter.username))
            .and(byTaskId(filter.taskId))
    }

    private fun byId(id: Long?): Specification<Project>? {
        if (id == null) return null

        return Specification { root, _, cr ->
            cr.equal(root.get<Long>(Project::id.name), id)
        }
    }

    private fun byName(name: String?): Specification<Project>? {
        if (name == null) return null

        return Specification { root, _, cr ->
            cr.like(root.get<String>(Project::name.name), "%$name%")
        }
    }

    private fun byDescription(description: String?): Specification<Project>? {
        if (description == null) return null

        return Specification { root, _, cr ->
            cr.like(root.get<String>(Project::description.name), "%$description%")
        }
    }

    private fun byCreatedAt(minCreatedAt: LocalDate?, maxCreatedAt: LocalDate?): Specification<Project>? {
        if (minCreatedAt == null && maxCreatedAt == null) return null

        if (minCreatedAt == null) {
            return Specification { root, _, cr ->
                cr.lessThanOrEqualTo(
                    root.get<LocalDateTime>(Project::createdAt.name),
                    maxCreatedAt!!.atTime(23, 59, 59)
                )
            }
        }

        if (maxCreatedAt == null) {
            return Specification { root, _, cr ->
                cr.greaterThanOrEqualTo(root.get<LocalDateTime>(Project::createdAt.name), minCreatedAt.atStartOfDay())
            }
        }

        return Specification { root, _, cr ->
            cr.between(
                root.get<LocalDateTime>(Project::createdAt.name),
                minCreatedAt.atStartOfDay(),
                maxCreatedAt.atTime(23, 59, 59)
            )
        }
    }

    private fun byUsername(username: String?): Specification<Project>? {
        if (username == null) return null

        return Specification { root, _, cr ->
            val join = root.join<Project, User>("users")
            cr.equal(join.get<String>("username"), username)
        }
    }

    private fun byTaskId(taskId: Long?): Specification<Project>? {
        if (taskId == null) return null

        return Specification { root, _, cr ->
            val join = root.join<Project, Task>("tasks")
            cr.equal(join.get<Long>("id"), taskId)
        }
    }
}