package com.umutsaydam.zenfocus.domain.repository.local

import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel

interface PomodoroSessionRepository {
    suspend fun upsertPomodoroSession(pomodoroSessionModel: PomodoroSessionModel)
    suspend fun getCountOfSessionsByDate(customDate: String): Int
    suspend fun getCountOfSessionsBetween2Dates(startDate: String, endDate: String): Int
    suspend fun getCountOfTotalPomodoroSessions(): Int
    suspend fun getCurrentStreak(): Int
    suspend fun getLongestStreak(): Int
}