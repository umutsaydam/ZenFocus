package com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadVibrateState(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readVibrateState()
    }
}