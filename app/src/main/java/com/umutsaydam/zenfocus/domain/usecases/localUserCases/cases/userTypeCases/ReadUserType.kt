package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadUserType(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readUserType()
    }
}