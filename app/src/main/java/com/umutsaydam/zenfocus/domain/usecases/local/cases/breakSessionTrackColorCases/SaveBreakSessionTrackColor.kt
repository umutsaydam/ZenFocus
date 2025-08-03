package com.umutsaydam.zenfocus.domain.usecases.local.cases.breakSessionTrackColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveBreakSessionTrackColor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(breakSessionTrackColorId: Int){
        localUserManager.saveBreakSessionTrackColorId(breakSessionTrackColorId)
    }
}