package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveUserId(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(userId: String){
        localUserManager.saveUserId(userId)
    }
}