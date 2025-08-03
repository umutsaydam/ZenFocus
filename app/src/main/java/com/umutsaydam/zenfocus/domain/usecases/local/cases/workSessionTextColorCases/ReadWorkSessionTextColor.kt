package com.umutsaydam.zenfocus.domain.usecases.local.cases.workSessionTextColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadWorkSessionTextColor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readWorkSessionTextColorId()
    }
}