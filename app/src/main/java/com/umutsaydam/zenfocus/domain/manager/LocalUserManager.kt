package com.umutsaydam.zenfocus.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveApp()
    fun readAppEntry(): Flow<Boolean>

    suspend fun saveVibrateState(isEnable: Boolean)
    fun readVibrateState(): Flow<Boolean>

    suspend fun saveAppLang(appLang: String)
    fun readAppLang(): Flow<String>

    suspend fun saveFocusSound(fileName: String)
    fun readFocusSound(): Flow<String>

    suspend fun saveUserId(userId: String)
    fun readUserId(): Flow<String>
    suspend fun deleteUserId()

    suspend fun saveUserType(userType: String)
    fun readUserType(): Flow<String>
    suspend fun deleteUserType()

    suspend fun saveTheme(themeName: String)
    fun readTheme(): Flow<String>

    suspend fun savePomodoroCycle(pomodoroCycle: Int)
    fun readPomodoroCycle(): Flow<Int>

    suspend fun savePomodoroWorkDuration(workDuration: Int)
    fun readPomodoroWorkDuration(): Flow<Int>

    suspend fun savePomodoroBreakDuration(breakDuration: Int)
    fun readPomodoroBreakDuration(): Flow<Int>
}