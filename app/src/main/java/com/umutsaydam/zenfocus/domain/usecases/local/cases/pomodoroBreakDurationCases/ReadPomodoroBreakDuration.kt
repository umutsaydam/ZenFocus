package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadPomodoroBreakDuration(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readPomodoroBreakDuration()
    }
}