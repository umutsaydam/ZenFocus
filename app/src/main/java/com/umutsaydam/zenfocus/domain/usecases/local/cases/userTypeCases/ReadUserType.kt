package com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadUserType(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readUserType()
    }
}