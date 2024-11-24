package com.umutsaydam.zenfocus.domain.usecases.tasks

data class ToDoUsesCases(
    val getTasks: GetTasks,
    val upsertTask: UpsertTask,
    val deleteTask: DeleteTask
)
