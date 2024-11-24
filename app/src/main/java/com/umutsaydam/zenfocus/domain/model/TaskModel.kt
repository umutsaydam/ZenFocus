package com.umutsaydam.zenfocus.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaskModel")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    val taskID: Int = 0,
    val taskContent: String = "",
    var isTaskCompleted: Boolean = false
)
