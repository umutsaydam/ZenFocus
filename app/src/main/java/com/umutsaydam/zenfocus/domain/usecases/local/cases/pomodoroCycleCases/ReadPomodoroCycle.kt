package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadPomodoroCycle(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readPomodoroCycle()
    }
}