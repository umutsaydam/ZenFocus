package com.umutsaydam.zenfocus.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.umutsaydam.zenfocus.data.local.mapper.UUIDConverter
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.TotalMinutesByDateModel

@Dao
@TypeConverters(UUIDConverter::class)
interface PomodoroSessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pomodoroSessionModel: PomodoroSessionModel)

    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE session_date = :customDate")
    suspend fun getCountOfSessionsByDate(customDate: String): Int

    @Query("SELECT DATE(session_date) AS pomodoroDate, (SUM(work_duration)/ 1000 / 60) as minute FROM pomodoro_sessions GROUP BY DATE(session_date) HAVING DATE(session_date) BETWEEN :startDate AND :endDate;")
    suspend fun getCountOfSessionsBetween2Dates(
        startDate: String,
        endDate: String
    ): List<TotalMinutesByDateModel>

    @Query("SELECT COUNT(*) FROM pomodoro_sessions")
    suspend fun getCountOfTotalPomodoroSessions(): Int

    @Query(
        """
            WITH OrderedSessions AS (
                SELECT
                    DATE(s1.session_date) AS session_date,
                    (SELECT DATE(MAX(s2.session_date)) FROM pomodoro_sessions s2 WHERE DATE(s2.session_date) < DATE(s1.session_date)) AS previousDate
                FROM pomodoro_sessions s1
                GROUP BY DATE(s1.session_date)
            ),
            StreakGroups AS (
                SELECT
                    session_date,
                    CASE
                        WHEN previousDate IS NULL OR JULIANDAY(session_date) - JULIANDAY(previousDate) > 1
                        THEN 1
                        ELSE 0
                    END AS group_change
                FROM OrderedSessions
            ),
            StreakGroupsWithRunningSum AS (
                SELECT
                    session_date,
                    (SELECT SUM(group_change) FROM StreakGroups AS sg WHERE sg.session_date <= s.session_date) AS group_num
                FROM StreakGroups AS s
            )
            SELECT
                CASE
                    WHEN (SELECT COUNT(*) FROM pomodoro_sessions WHERE DATE(session_date) = DATE('now')) = 0
                    AND NOT EXISTS (
                        SELECT 1 FROM pomodoro_sessions WHERE DATE(session_date) = DATE('now', '-1 day')
                    )
                    THEN 0
                    ELSE (
                        SELECT COUNT(*)
                        FROM StreakGroupsWithRunningSum
                        WHERE group_num = (
                            SELECT group_num
                            FROM StreakGroupsWithRunningSum
                            WHERE session_date = (
                                SELECT DATE(MAX(session_date))
                                FROM pomodoro_sessions
                                WHERE DATE(session_date) <= DATE('now')
                            )
                        )
                    )
                END AS streak;
        """
    )
    suspend fun getCurrentStreak(): Int


    @Query(
        """
            WITH OrderedSessions AS (
                SELECT DISTINCT DATE(session_date) AS session_date
                FROM pomodoro_sessions
            ),
            StreakGroups AS (
                SELECT 
                    session_date,
                    JULIANDAY(session_date) - (SELECT COUNT(*) FROM OrderedSessions o2 WHERE o2.session_date <= o1.session_date) AS streak_id
                FROM OrderedSessions o1
            )
            SELECT MAX(streak_count) AS max_streak
            FROM (
                SELECT COUNT(*) AS streak_count
                FROM StreakGroups
                GROUP BY streak_id
            )
        """
    )
    suspend fun getLongestStreak(): Int

    @Query(
        """
            WITH WeekBounds AS (
                SELECT 
                    DATE(:selectedDate, 'weekday 0', '-6 days') AS week_start,
                    DATE(:selectedDate, 'weekday 0') AS week_end
            )
            SELECT DATE(session_date) AS pomodoroDate, 
                   (SUM(work_duration) / 1000 / 60) AS minute
            FROM pomodoro_sessions
            WHERE DATE(session_date) BETWEEN (SELECT week_start FROM WeekBounds) 
                                  AND (SELECT week_end FROM WeekBounds)
            GROUP BY DATE(session_date)
            """
    )
    suspend fun getThisWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel>

    @Query(
        """
            WITH CurrentWeek AS (
                SELECT 
                    DATE(:selectedDate, 'weekday 0', '-6 days') AS this_week_start,
                    DATE(:selectedDate, 'weekday 0') AS this_week_end
            ),
            LastWeek AS (
                SELECT 
                    DATE(this_week_start, '-7 days') AS last_week_start, 
                    DATE(this_week_end, '-7 days') AS last_week_end
                FROM CurrentWeek
            )
            SELECT DATE(session_date) AS pomodoroDate, 
                   (SUM(work_duration) / 1000 / 60) AS minute
            FROM pomodoro_sessions
            WHERE session_date BETWEEN (SELECT last_week_start FROM LastWeek) 
                                  AND (SELECT last_week_end FROM LastWeek)
            GROUP BY DATE(session_date)
        """
    )
    suspend fun getLastWeekStatistics(selectedDate: String): List<TotalMinutesByDateModel>

    @Query(
        """
            WITH CurrentMonth AS (
            SELECT 
                DATE(:selectedDate, 'start of month') AS this_month_start,
                DATE(:selectedDate, 'start of month', '+1 month', '-1 day') AS this_month_end
            )
            SELECT DATE(session_date) AS pomodoroDate, 
                   (SUM(work_duration) / 1000 / 60) AS minute
            FROM pomodoro_sessions
            WHERE DATE(session_date) BETWEEN (SELECT this_month_start FROM CurrentMonth) 
                                         AND (SELECT this_month_end FROM CurrentMonth)
            GROUP BY DATE(session_date)
        """
    )
    suspend fun getThisMonthStatistics(selectedDate: String): List<TotalMinutesByDateModel>

    @Query("SELECT * FROM pomodoro_sessions")
    suspend fun getAllSessions(): List<PomodoroSessionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<PomodoroSessionModel>)
}