package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.TaskMapper
import com.dvo.taskManagerPro.repository.LabelRepository
import com.dvo.taskManagerPro.repository.TaskRepository
import com.dvo.taskManagerPro.repository.TaskSpecification
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.service.TaskService
import com.dvo.taskManagerPro.web.model.filter.TaskFilter
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val labelRepository: LabelRepository,
    private val taskMapper: TaskMapper
) : TaskService {
    private val log = LoggerFactory.getLogger(TaskServiceImpl::class.java)

    override fun findAll(): List<Task> {
        log.info("Call findAll in TaskServiceImpl")

        return taskRepository.findAll()
    }

    override fun findAllByFilter(filter: TaskFilter): List<Task> {
        log.info("Call findAllByFilter in TaskServiceImpl with filter: {}", filter)

        val spec = TaskSpecification.withFilter(filter) ?: Specification.where(null)
        return taskRepository.findAll(
            spec,
            PageRequest.of(filter.pageNumber!!, filter.pageSize!!)
        ).content
    }

    override fun findById(id: Long): Task {
        log.info("Call findById in TaskServiceImpl with ID: {}", id)

        return taskRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $id not found")
            }
    }

    @Transactional
    override fun create(task: Task): Task {
        log.info("Call create in TaskServiceImpl with task title: {}", task.title)

        if (taskRepository.existsByTitle(task.title)) {
            throw EntityExistsException("Task with title: ${task.title} already exists")
        }

        return taskRepository.save(task)
    }

    @Transactional
    override fun update(task: UpdateTaskRequest, id: Long): Task {
        log.info("Call update in TaskServiceImpl with ID: {} and task: {}", id, task)

        val existedTask = taskRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $id not found")
            }

        if (task.title != null && taskRepository.existsByTitle(task.title) && task.title != existedTask.title) {
            throw EntityExistsException("Task with title: ${task.title} already exists")
        }

        taskMapper.updateRequestToTask(task, existedTask)

        return taskRepository.save(existedTask)
    }

    @Transactional
    override fun deleteById(id: Long) {
        log.info("Call deleteById in TaskServiceImpl with ID: {}", id)

        taskRepository.deleteById(id)
    }

    @Transactional
    override fun assignTaskToUser(userId: Long, taskId: Long) {
        log.info("Call assignTaskToUser in TaskServiceImpl with userId: {} and taskId: {}", userId, taskId)

        val user = userRepository.findById(userId)
            .orElseThrow {
                EntityNotFoundException("User with ID: $userId not found")
            }

        val task = taskRepository.findById(taskId)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $taskId not found")
            }

        val updatedTask = task.copy(assignedTo = user)
        taskRepository.save(updatedTask)
    }

    @Transactional
    override fun unassignTaskFromUser(taskId: Long) {
        log.info("Call unassignTaskFromUser in TaskServiceImpl with taskId: {}", taskId)

        val task = taskRepository.findById(taskId)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $taskId not found")
            }

        val updatedTask = task.copy(assignedTo = null)
        taskRepository.save(updatedTask)
    }

    override fun getTasksByUser(userId: Long): List<Task> {
        log.info("Call getTasksByUser in TaskServiceImpl with userId: {}", userId)

        return taskRepository.findByAssignedToId(userId)
    }

    @Transactional
    override fun assignLabelToTask(taskId: Long, labelId: Long) {
        log.info("Call assignLabelToTask in TaskServiceImpl with taskId: {} and labelId: {}", taskId, labelId)

        val label = labelRepository.findById(labelId)
            .orElseThrow {
                EntityNotFoundException("Label with ID: $labelId not found")
            }

        val task = taskRepository.findById(taskId)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $taskId not found")
            }

        if (!task.labels.contains(label)) {
            task.labels.add(label)
            taskRepository.save(task)
        }
    }

    @Transactional
    override fun unassignLabelFromTask(taskId: Long, labelId: Long) {
        log.info("Call unassignLabelFromTask in TaskServiceImpl with taskId: {} and labelId: {}", taskId, labelId)


        val label = labelRepository.findById(labelId)
            .orElseThrow {
                EntityNotFoundException("Label with ID: $labelId not found")
            }

        val task = taskRepository.findById(taskId)
            .orElseThrow {
                EntityNotFoundException("Task with ID: $taskId not found")
            }

        if (task.labels.contains(label)) {
            task.labels.remove(label)
            taskRepository.save(task)
        }
    }
}