package com.umutsaydam.zenfocus.domain.usecases.local.cases.themeCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveTheme(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(themeName: String) {
        localUserManager.saveTheme(themeName)
    }
}