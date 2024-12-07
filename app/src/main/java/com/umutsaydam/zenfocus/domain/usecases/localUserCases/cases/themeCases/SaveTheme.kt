package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.themeCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveTheme(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(themeName: String) {
        localUserManager.saveTheme(themeName)
    }
}