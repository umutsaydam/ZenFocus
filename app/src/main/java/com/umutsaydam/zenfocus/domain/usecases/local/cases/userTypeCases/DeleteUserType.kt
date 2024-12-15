package com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class DeleteUserType(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.deleteUserType()
    }
}