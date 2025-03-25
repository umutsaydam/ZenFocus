package com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions

import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.repository.local.PomodoroSessionRepository

interface PomodoroSessionsUseCases {
    suspend fun upsertPomodoroSession(pomodoroSessionModel: PomodoroSessionModel)
    suspend fun getCountOfSessionsByDate(customDate: String): Int
    suspend fun getCountOfSessionsBetween2Dates(startDate: String, endDate: String): Int
    suspend fun getCountOfTotalPomodoroSessions(): Int
    suspend fun getCurrentStreak(): Int
    suspend fun getLongestStreak(): Int
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

    override suspend fun getCountOfSessionsBetween2Dates(startDate: String, endDate: String): Int {
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

}