package com.umutsaydam.zenfocus.domain.usecases.tasks.cases

import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository

class GetTasks(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(): List<TaskModel> {
        return toDoRepository.getTasks()
    }
}