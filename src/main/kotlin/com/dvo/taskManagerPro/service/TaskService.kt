package com.dvo.taskManagerPro.service

import com.dvo.taskManagerPro.entity.Task
import com.dvo.taskManagerPro.web.model.request.UpdateTaskRequest

interface TaskService {
    fun findAll(): List<Task>
    fun findById(id: Long): Task
    fun create(task: Task): Task
    fun update(task: UpdateTaskRequest, id: Long): Task
    fun deleteById(id: Long)
    fun assignTaskToUser(userId: Long, taskId: Long)
    fun unassignTaskFromUser(taskId: Long)
    fun getTasksByUser(userId: Long): List<Task>
    fun assignLabelToTask(taskId: Long, labelId: Long)
    fun unassignLabelFromTask(taskId: Long, labelId: Long)
}