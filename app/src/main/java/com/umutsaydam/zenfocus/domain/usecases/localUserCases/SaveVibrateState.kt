package com.umutsaydam.zenfocus.domain.usecases.localUserCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveVibrateState(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(isEnable: Boolean) {
        localUserManager.saveVibrateState(isEnable)
    }
}