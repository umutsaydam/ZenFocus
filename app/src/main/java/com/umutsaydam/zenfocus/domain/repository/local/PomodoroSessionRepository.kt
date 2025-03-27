package com.umutsaydam.zenfocus.domain.repository.local

import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.TotalMinutesByDateModel

interface PomodoroSessionRepository {
    suspend fun upsertPomodoroSession(pomodoroSessionModel: PomodoroSessionModel)
    suspend fun getCountOfSessionsByDate(customDate: String): Int
    suspend fun getCountOfSessionsBetween2Dates(
        startDate: String,
        endDate: String
    ): List<TotalMinutesByDateModel>

    suspend fun getCountOfTotalPomodoroSessions(): Int
    suspend fun getCurrentStreak(): Int
    suspend fun getLongestStreak(): Int
    suspend fun getThisWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel>
    suspend fun getLastWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel>
    suspend fun getThisMonthStatistics(selectedDate: String): List<TotalMinutesByDateModel>
}