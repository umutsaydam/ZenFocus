package com.umutsaydam.zenfocus.domain.usecases.tasks

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.ToDoRepository

class GetTasks(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(): List<TaskModel> {
        return toDoRepository.getTasks()
    }
}