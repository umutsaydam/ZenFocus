package com.umutsaydam.zenfocus.domain.usecases.localUserCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveAppLang(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(appLang: String) {
        localUserManager.saveAppLang(appLang)
    }
}