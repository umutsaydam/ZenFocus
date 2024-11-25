package com.umutsaydam.zenfocus.data.local

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.ToDoRepository

class ToDoRepositoryImpl(
    private val tasksDao: TasksDao
) : ToDoRepository {
    override suspend fun getTasks(): List<TaskModel> {
        return tasksDao.getTasks()
    }

    override suspend fun upsertTask(taskModel: TaskModel) {
        tasksDao.upsert(taskModel)
    }

    override suspend fun deleteTask(taskModel: TaskModel) {
        tasksDao.deleteTask(taskModel)
    }
}