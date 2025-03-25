package com.umutsaydam.zenfocus.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel

@Dao
interface PomodoroSessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pomodoroSessionModel: PomodoroSessionModel)

    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE session_date = :customDate")
    fun getCountOfSessionsByDate(customDate: String): Int

    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE session_date BETWEEN :startDate AND :endDate")
    fun getCountOfSessionsBetween2Dates(startDate: String, endDate: String): Int

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
                    SELECT
                        DATE(session_date) AS session_date
                    FROM pomodoro_sessions
                    GROUP BY DATE(session_date)
                    ORDER BY session_date
                ),
                StreakGroups AS (
                    SELECT
                        session_date,
                        (SELECT DATE(MAX(session_date)) 
                         FROM OrderedSessions s2 
                         WHERE JULIANDAY(s2.session_date) < JULIANDAY(s1.session_date)) AS previousDate
                    FROM OrderedSessions s1
                ),
                StreakChange AS (
                    SELECT
                        session_date,
                        CASE
                            WHEN previousDate IS NULL OR JULIANDAY(session_date) - JULIANDAY(previousDate) > 1
                            THEN 1
                            ELSE 0
                        END AS group_change
                    FROM StreakGroups
                ),
                Streaks AS (
                    SELECT
                        session_date,
                        SUM(group_change) AS streak_group
                    FROM StreakChange
                    GROUP BY group_change, session_date
                )
                SELECT
                    MAX(streak_count) AS max_streak
                FROM (
                    SELECT
                        COUNT(*) AS streak_count
                    FROM Streaks
                    GROUP BY streak_group
                ) AS streak_counts;
                """
    )
    suspend fun getLongestStreak(): Int
}