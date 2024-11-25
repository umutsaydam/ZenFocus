package com.umutsaydam.zenfocus.domain.usecases.localUserCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppLang(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readAppLang()
    }
}