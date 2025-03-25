package com.umutsaydam.zenfocus.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSessionModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "session_id")
    val sessionId: Int = 0,
    @ColumnInfo(name = "session_duration")
    val sessionDuration: Long,
    @ColumnInfo(name = "session_date")
    val sessionDate: String
)
