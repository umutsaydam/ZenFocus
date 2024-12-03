package com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadUserId(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String>{
        return localUserManager.readUserId()
    }
}