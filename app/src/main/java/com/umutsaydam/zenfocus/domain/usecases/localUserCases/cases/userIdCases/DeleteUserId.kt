package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class DeleteUserId(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.deleteUserId()
    }
}