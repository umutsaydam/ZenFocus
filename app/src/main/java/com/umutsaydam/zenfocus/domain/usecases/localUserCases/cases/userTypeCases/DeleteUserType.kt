package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class DeleteUserType(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.deleteUserType()
    }
}