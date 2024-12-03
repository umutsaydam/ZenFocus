package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appEntryCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveApp()
    }
}