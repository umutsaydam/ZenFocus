package com.umutsaydam.zenfocus.data.local.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import com.umutsaydam.zenfocus.util.Constants
import com.umutsaydam.zenfocus.util.Constants.APP_ENTRY
import com.umutsaydam.zenfocus.util.Constants.APP_LANG_ENGLISH
import com.umutsaydam.zenfocus.util.Constants.DEFAULT_POMODORO_BREAK_DURATION
import com.umutsaydam.zenfocus.util.Constants.DEFAULT_POMODORO_CYCLE
import com.umutsaydam.zenfocus.util.Constants.DEFAULT_POMODORO_WORK_DURATION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
) : LocalUserManager {
    override suspend fun saveApp() {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.APP_ENTRY] ?: false
        }
    }

    override suspend fun saveVibrateState(isEnable: Boolean) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.VIBRATE_STATE] = isEnable
        }
    }

    override fun readVibrateState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.VIBRATE_STATE] ?: false
        }
    }

    override suspend fun saveAppLang(appLang: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.APP_LANG] = appLang
        }
    }

    override fun readAppLang(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.APP_LANG] ?: APP_LANG_ENGLISH
        }
    }

    override suspend fun saveUserId(userId: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.USER_ID] = userId
        }
    }

    override fun readUserId(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_ID] ?: ""
        }
    }

    override suspend fun deleteUserId() {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.USER_ID] = ""
        }
    }

    override suspend fun saveUserType(userType: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.USER_TYPE] = userType
        }
    }

    override fun readUserType(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_TYPE] ?: ""
        }
    }

    override suspend fun deleteUserType() {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.USER_TYPE] = ""
        }
    }

    override suspend fun saveTheme(themeName: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.FOCUS_THEME] = themeName
        }
    }

    override fun readTheme(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FOCUS_THEME] ?: Constants.DEFAULT_THEME
        }
    }

    override suspend fun savePomodoroCycle(pomodoroCycle: Int) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.POMODORO_CYCLE] = pomodoroCycle
        }
    }

    override fun readPomodoroCycle(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.POMODORO_CYCLE] ?: DEFAULT_POMODORO_CYCLE
        }
    }

    override suspend fun savePomodoroWorkDuration(workDuration: Int) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.POMODORO_WORK_DURATION] = workDuration
        }
    }

    override fun readPomodoroWorkDuration(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.POMODORO_WORK_DURATION] ?: DEFAULT_POMODORO_WORK_DURATION
        }
    }

    override suspend fun savePomodoroBreakDuration(breakDuration: Int) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.POMODORO_BREAK_DURATION] = breakDuration
        }
    }

    override fun readPomodoroBreakDuration(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.POMODORO_BREAK_DURATION] ?: DEFAULT_POMODORO_BREAK_DURATION
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_ENTRY)

private object PreferencesKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
    val VIBRATE_STATE = booleanPreferencesKey(name = Constants.VIBRATE_STATE)
    val APP_LANG = stringPreferencesKey(name = Constants.APP_LANG)
    val USER_ID = stringPreferencesKey(name = Constants.USER_ID)
    val USER_TYPE = stringPreferencesKey(name = Constants.USER_TYPE)
    val FOCUS_THEME = stringPreferencesKey(name = Constants.FOCUS_THEME)
    val POMODORO_CYCLE = intPreferencesKey(name = Constants.POMODORO_CYCLE)
    val POMODORO_WORK_DURATION = intPreferencesKey(name = Constants.POMODORO_WORK_DURATION)
    val POMODORO_BREAK_DURATION = intPreferencesKey(name = Constants.POMODORO_BREAK_DURATION)
}