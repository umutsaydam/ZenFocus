package com.umutsaydam.zenfocus.domain.localUserManager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveApp()
    fun readAppEntry(): Flow<Boolean>

    suspend fun saveVibrateState(isEnable: Boolean)
    fun readVibrateState(): Flow<Boolean>

    suspend fun saveAppLang(appLang: String)
    fun readAppLang(): Flow<String>

    suspend fun saveUserId(userId: String)
    fun readUserId(): Flow<String>
    suspend fun deleteUserId()

    suspend fun saveUserType(userType: String)
    fun readUserType(): Flow<String>
    suspend fun deleteUserType()
}