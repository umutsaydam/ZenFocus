package com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions

import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.TotalMinutesByDateModel
import com.umutsaydam.zenfocus.domain.repository.local.PomodoroSessionRepository

interface PomodoroSessionsUseCases {
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
    suspend fun getAllSessions(): List<PomodoroSessionModel>
    suspend fun insertAllSessions(sessions: List<PomodoroSessionModel>)
}

class PomodoroSessionsUseCasesImpl(
    private val sessionRepository: PomodoroSessionRepository
) : PomodoroSessionsUseCases {
    override suspend fun upsertPomodoroSession(pomodoroSessionModel: PomodoroSessionModel) {
        sessionRepository.upsertPomodoroSession(pomodoroSessionModel)
    }

    override suspend fun getCountOfSessionsByDate(customDate: String): Int {
        return sessionRepository.getCountOfSessionsByDate(customDate)
    }

    override suspend fun getCountOfSessionsBetween2Dates(
        startDate: String,
        endDate: String
    ): List<TotalMinutesByDateModel> {
        return sessionRepository.getCountOfSessionsBetween2Dates(startDate, endDate)
    }

    override suspend fun getCountOfTotalPomodoroSessions(): Int {
        return sessionRepository.getCountOfTotalPomodoroSessions()
    }

    override suspend fun getCurrentStreak(): Int {
        return sessionRepository.getCurrentStreak()
    }

    override suspend fun getLongestStreak(): Int {
        return sessionRepository.getLongestStreak()
    }

    override suspend fun getThisWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return sessionRepository.getThisWeekStatistics(selectedDate)
    }

    override suspend fun getLastWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return sessionRepository.getLastWeekStatistics(selectedDate)
    }

    override suspend fun getThisMonthStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return sessionRepository.getThisMonthStatistics(selectedDate)
    }

    override suspend fun getAllSessions(): List<PomodoroSessionModel> {
        return sessionRepository.getAllSessions()
    }

    override suspend fun insertAllSessions(sessions: List<PomodoroSessionModel>) {
        sessionRepository.insertAllSessions(sessions)
    }
}