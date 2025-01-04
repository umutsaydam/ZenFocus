package com.umutsaydam.zenfocus.domain.repository.local

import com.umutsaydam.zenfocus.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    suspend fun getTasks(): Flow<List<TaskModel>>

    suspend fun upsertTask(taskModel: TaskModel)

    suspend fun deleteTask(taskModel: TaskModel)
}