package com.umutsaydam.zenfocus.domain.usecases.local.cases.timeOutRingerStateCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadTimeOutRingerState(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean>{
        return localUserManager.readTimeOutRingerState()
    }
}