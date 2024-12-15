package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadPomodoroWorkDuration(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readPomodoroWorkDuration()
    }
}