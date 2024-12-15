package com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppLang(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readAppLang()
    }
}