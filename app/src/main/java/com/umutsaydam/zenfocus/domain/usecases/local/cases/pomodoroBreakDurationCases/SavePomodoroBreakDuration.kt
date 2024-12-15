package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SavePomodoroBreakDuration(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(breakDuration: Int) {
        localUserManager.savePomodoroBreakDuration(breakDuration)
    }
}