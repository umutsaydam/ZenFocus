package com.umutsaydam.zenfocus.domain.usecases.tasks.cases

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository

class UpsertTask(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(taskModel: TaskModel) {
        toDoRepository.upsertTask(taskModel)
    }
}