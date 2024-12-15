package com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveUserId(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(userId: String){
        localUserManager.saveUserId(userId)
    }
}