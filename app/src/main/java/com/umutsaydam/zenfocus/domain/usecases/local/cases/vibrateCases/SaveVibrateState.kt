package com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveVibrateState(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(isEnable: Boolean) {
        localUserManager.saveVibrateState(isEnable)
    }
}