package com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadUserId(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String>{
        return localUserManager.readUserId()
    }
}