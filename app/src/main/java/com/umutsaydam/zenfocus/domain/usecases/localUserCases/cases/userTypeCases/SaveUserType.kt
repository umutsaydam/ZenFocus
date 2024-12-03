package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager

class SaveUserType(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(userType: String) {
        localUserManager.saveUserType(userType)
    }
}