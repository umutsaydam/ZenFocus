package com.umutsaydam.zenfocus.domain.usecases.local.cases.workSessionTrackColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveWorkSessionTrackColor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(workSessionTrackColorId: Int) {
        localUserManager.saveWorkSessionTrackColorId(workSessionTrackColorId)
    }
}