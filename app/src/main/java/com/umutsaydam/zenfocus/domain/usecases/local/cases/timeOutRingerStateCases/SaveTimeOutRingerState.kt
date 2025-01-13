package com.umutsaydam.zenfocus.domain.usecases.local.cases.timeOutRingerStateCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveTimeOutRingerState(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(isEnabled: Boolean){
        localUserManager.saveTimeOutRingerState(isEnabled)
    }
}