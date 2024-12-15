package com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveAppLang(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(appLang: String) {
        localUserManager.saveAppLang(appLang)
    }
}