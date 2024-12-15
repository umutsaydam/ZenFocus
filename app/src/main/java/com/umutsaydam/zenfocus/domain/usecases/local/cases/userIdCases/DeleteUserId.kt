package com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class DeleteUserId(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.deleteUserId()
    }
}