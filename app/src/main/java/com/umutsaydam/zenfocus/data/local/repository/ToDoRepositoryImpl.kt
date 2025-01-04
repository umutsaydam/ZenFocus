package com.umutsaydam.zenfocus.data.local.repository

import com.umutsaydam.zenfocus.data.local.db.TasksDao
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import kotlinx.coroutines.flow.Flow

class ToDoRepositoryImpl(
    private val tasksDao: TasksDao
) : ToDoRepository {
    override suspend fun getTasks(): Flow<List<TaskModel>> {
        return tasksDao.getTasks()
    }

    override suspend fun upsertTask(taskModel: TaskModel) {
        tasksDao.upsert(taskModel)
    }

    override suspend fun deleteTask(taskModel: TaskModel) {
        tasksDao.deleteTask(taskModel)
    }
}