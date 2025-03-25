package com.umutsaydam.zenfocus.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.TaskModel

@Database(entities = [TaskModel::class, PomodoroSessionModel::class], version = 2, exportSchema = false)
abstract class ZenFocusDB : RoomDatabase() {
    abstract val tasksDao: TasksDao
    abstract val pomodoroSessionsDao: PomodoroSessionsDao
}