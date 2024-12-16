package com.umutsaydam.zenfocus.domain.usecases.local.cases.focusSoundCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveFocusSound(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(fileName: String) {
        localUserManager.saveFocusSound(fileName)
    }
}