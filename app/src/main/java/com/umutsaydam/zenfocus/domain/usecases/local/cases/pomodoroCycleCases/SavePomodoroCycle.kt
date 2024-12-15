package com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SavePomodoroCycle(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(pomodoroCycle: Int) {
        localUserManager.savePomodoroCycle(pomodoroCycle)
    }
}