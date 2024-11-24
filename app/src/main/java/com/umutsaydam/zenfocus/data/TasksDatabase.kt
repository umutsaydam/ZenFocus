package com.umutsaydam.zenfocus.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.umutsaydam.zenfocus.domain.model.TaskModel

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract val tasksDao: TasksDao
}