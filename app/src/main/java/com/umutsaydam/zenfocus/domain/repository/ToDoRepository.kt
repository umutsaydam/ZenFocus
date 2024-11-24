package com.umutsaydam.zenfocus.domain.repository

import com.umutsaydam.zenfocus.domain.model.TaskModel

interface ToDoRepository {
    suspend fun getTasks(): List<TaskModel>

    suspend fun upsertTask(taskModel: TaskModel)

    suspend fun deleteTask(taskModel: TaskModel)
}