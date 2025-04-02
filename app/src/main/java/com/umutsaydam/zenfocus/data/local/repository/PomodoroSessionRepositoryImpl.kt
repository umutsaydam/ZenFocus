package com.umutsaydam.zenfocus.data.local.repository

import com.umutsaydam.zenfocus.data.local.db.PomodoroSessionsDao
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.TotalMinutesByDateModel
import com.umutsaydam.zenfocus.domain.repository.local.PomodoroSessionRepository

class PomodoroSessionRepositoryImpl(
    private val pomodoroSessionsDao: PomodoroSessionsDao
) : PomodoroSessionRepository {

    override suspend fun upsertPomodoroSession(pomodoroSessionModel: PomodoroSessionModel) {
        pomodoroSessionsDao.upsert(pomodoroSessionModel)
    }

    override suspend fun getCountOfSessionsByDate(customDate: String): Int {
        return pomodoroSessionsDao.getCountOfSessionsByDate(customDate)
    }

    override suspend fun getCountOfSessionsBetween2Dates(
        startDate: String,
        endDate: String
    ): List<TotalMinutesByDateModel> {
        return pomodoroSessionsDao.getCountOfSessionsBetween2Dates(startDate, endDate)
    }

    override suspend fun getCountOfTotalPomodoroSessions(): Int {
        return pomodoroSessionsDao.getCountOfTotalPomodoroSessions()
    }

    override suspend fun getCurrentStreak(): Int {
        return pomodoroSessionsDao.getCurrentStreak()
    }

    override suspend fun getLongestStreak(): Int {
        return pomodoroSessionsDao.getLongestStreak()
    }

    override suspend fun getThisWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return pomodoroSessionsDao.getThisWeekStatistics(selectedDate)
    }

    override suspend fun getLastWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return pomodoroSessionsDao.getLastWeekStatistics(selectedDate)
    }

    override suspend fun getThisMonthStatistics(selectedDate: String): List<TotalMinutesByDateModel> {
        return pomodoroSessionsDao.getThisMonthStatistics(selectedDate)
    }

    override suspend fun getAllSessions(): List<PomodoroSessionModel> {
        return pomodoroSessionsDao.getAllSessions()
    }

    override suspend fun insertAllSessions(sessions: List<PomodoroSessionModel>)  {
        pomodoroSessionsDao.insertAll(sessions)
    }
}