package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.themeCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadTheme(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readTheme()
    }
}