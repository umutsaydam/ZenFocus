package com.umutsaydam.zenfocus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umutsaydam.zenfocus.domain.model.TaskModel

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(taskModel: TaskModel)

    @Delete
    suspend fun deleteTask(taskModel: TaskModel)

    @Query("SELECT * FROM TaskModel")
    suspend fun getTasks(): List<TaskModel>
}