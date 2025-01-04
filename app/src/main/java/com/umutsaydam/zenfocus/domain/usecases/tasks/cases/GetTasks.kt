package com.umutsaydam.zenfocus.domain.usecases.tasks.cases

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import kotlinx.coroutines.flow.Flow

class GetTasks(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(): Flow<List<TaskModel>> {
        return toDoRepository.getTasks()
    }
}