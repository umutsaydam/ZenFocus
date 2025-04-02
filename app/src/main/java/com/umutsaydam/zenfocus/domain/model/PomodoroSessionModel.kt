package com.umutsaydam.zenfocus.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSessionModel(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "work_duration")
    val workDuration: Long,
    @ColumnInfo(name = "break_duration")
    val breakDuration: Long,
    @ColumnInfo(name = "session_date")
    val sessionDate: String
)
