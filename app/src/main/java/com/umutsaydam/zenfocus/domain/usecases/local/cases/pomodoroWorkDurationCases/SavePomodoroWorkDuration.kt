package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SavePomodoroWorkDuration(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(workDuration: Int) {
        localUserManager.savePomodoroWorkDuration(workDuration)
    }
}