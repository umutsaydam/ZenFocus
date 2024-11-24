package com.umutsaydam.zenfocus.domain.usecases.tasks

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.ToDoRepository

class DeleteTask(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(
        taskModel: TaskModel
    ) {
        toDoRepository.deleteTask(taskModel)
    }
}