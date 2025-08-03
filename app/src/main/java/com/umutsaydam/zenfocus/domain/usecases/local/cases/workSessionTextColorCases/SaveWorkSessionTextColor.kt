package com.umutsaydam.zenfocus.domain.usecases.local.cases.workSessionTextColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveWorkSessionTextColor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(workTextSessionTrackColorId: Int){
        localUserManager.saveWorkSessionTextColorId(workTextSessionTrackColorId)
    }
}