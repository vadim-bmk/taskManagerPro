package com.dvo.taskManagerPro.repository

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.entity.Project
import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.entity.User
import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime

object TaskSpecification {

    fun withFilter(filter: TaskFilter): Specification<Task> {
        return Specification.where(byId(filter.id))
            .and(byTitle(filter.title))
            .and(byDescription(filter.description))
            .and(byCreatedAt(filter.minCreatedAt, filter.maxCreatedAt))
            .and(byDueData(filter.minDueDate, filter.maxDueDate))
            .and(byStatus(filter.status))
            .and(byPriority(filter.priority))
            .and(byUsername(filter.username))
            .and(byProjectId(filter.projectId))
            .and(byLabelId(filter.labelId))

    }

    private fun byId(id: Long?): Specification<Task>? {
        if (id == null) return null

        return Specification { root, _, cr ->
            cr.equal(root.get<Long>(Task::id.name), id)
        }
    }

    private fun byTitle(title: String?): Specification<Task>? {
        if (title == null) return null

        return Specification { root, _, cr ->
            cr.like(root.get<String>(Task::title.name), "%$title%")
        }
    }

    private fun byDescription(description: String?): Specification<Task>? {
        if (description == null) return null

        return Specification { root, _, cr ->
            cr.like(root.get<String>(Task::description.name), "%$description%")
        }
    }

    private fun byCreatedAt(minCreatedAt: LocalDate?, maxCreatedAt: LocalDate?): Specification<Task>? {
        if (minCreatedAt == null && maxCreatedAt == null) return null

        if (minCreatedAt == null) {
            return Specification { root, _, cr ->
                cr.lessThanOrEqualTo(root.get<LocalDateTime>(Task::createdAt.name), maxCreatedAt!!.atTime(23, 59, 59))
            }
        }

        if (maxCreatedAt == null) {
            return Specification { root, _, cr ->
                cr.greaterThanOrEqualTo(root.get<LocalDateTime>(Task::createdAt.name), minCreatedAt.atStartOfDay())
            }
        }

        return Specification { root, _, cr ->
            cr.between(
                root.get<LocalDateTime>(Task::createdAt.name),
                minCreatedAt.atStartOfDay(),
                maxCreatedAt.atTime(23, 59, 59)
            )
        }
    }

    private fun byDueData(minDueDate: LocalDate?, maxDueDate: LocalDate?): Specification<Task>? {
        if (minDueDate == null && maxDueDate == null) return null

        if (minDueDate == null) {
            return Specification { root, _, cr ->
                cr.lessThanOrEqualTo(root.get<LocalDate>(Task::dueDate.name), maxDueDate)
            }
        }

        if (maxDueDate == null) {
            return Specification { root, _, cr ->
                cr.greaterThanOrEqualTo(root.get<LocalDate>(Task::dueDate.name), minDueDate)
            }
        }

        return Specification { root, _, cr ->
            cr.between(root.get<LocalDate>(Task::dueDate.name), minDueDate, maxDueDate)
        }
    }

    private fun byStatus(status: String?): Specification<Task>? {
        if (status == null) return null

        return Specification { root, _, cr ->
            cr.equal(root.get<String>(Task::status.name), status)
        }
    }

    private fun byPriority(priority: String?): Specification<Task>? {
        if (priority == null) return null

        return Specification { root, _, cr ->
            cr.equal(root.get<String>(Task::priority.name), priority)
        }
    }

    private fun byUsername(username: String?): Specification<Task>? {
        if (username == null) return null

        return Specification { root, _, cr ->
            val join = root.join<Task, User>("assignedTo")
            cr.equal(join.get<String>("username"), username)
        }
    }

    private fun byProjectId(projectId: Long?): Specification<Task>? {
        if (projectId == null) return null

        return Specification { root, _, cr ->
            val join = root.join<Task, Project>("project")
            cr.equal(join.get<Long>("id"), projectId)
        }
    }

    private fun byLabelId(labelId: Long?): Specification<Task>? {
        if (labelId == null) return null

        return Specification { root, _, cr ->
            val join = root.join<Task, Label>("labels")
            cr.equal(join.get<Long>("id"), labelId)
        }
    }
}