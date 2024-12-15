package com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager

class SaveUserType(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(userType: String) {
        localUserManager.saveUserType(userType)
    }
}