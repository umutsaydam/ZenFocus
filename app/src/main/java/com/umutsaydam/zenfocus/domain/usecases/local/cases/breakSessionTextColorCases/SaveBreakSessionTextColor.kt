package com.umutsaydam.zenfocus.domain.usecases.local.cases.breakSessionTextColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveBreakSessionTextColor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(breakTextSessionTrackColorId: Int){
        localUserManager.saveBreakSessionTextColorId(breakTextSessionTrackColorId)
    }
}