package com.umutsaydam.zenfocus.data.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import com.umutsaydam.zenfocus.util.Constants
import com.umutsaydam.zenfocus.util.Constants.APP_ENTRY
import com.umutsaydam.zenfocus.util.Constants.APP_LANG_ENGLISH
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
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_ENTRY)

private object PreferencesKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
    val VIBRATE_STATE = booleanPreferencesKey(name = Constants.VIBRATE_STATE)
    val APP_LANG = stringPreferencesKey(name = Constants.APP_LANG)
    val USER_ID = stringPreferencesKey(name = Constants.USER_ID)
    val USER_TYPE = stringPreferencesKey(name = Constants.USER_TYPE)
    val FOCUS_THEME = stringPreferencesKey(name = Constants.FOCUS_THEME)
}