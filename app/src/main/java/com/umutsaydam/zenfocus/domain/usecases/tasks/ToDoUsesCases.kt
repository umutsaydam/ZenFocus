package com.umutsaydam.zenfocus.domain.usecases.tasks

import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.DeleteTask
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.GetTasks
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.UpsertTask

data class ToDoUsesCases(
    val getTasks: GetTasks,
    val upsertTask: UpsertTask,
    val deleteTask: DeleteTask
)
