package com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveApp()
    }
}