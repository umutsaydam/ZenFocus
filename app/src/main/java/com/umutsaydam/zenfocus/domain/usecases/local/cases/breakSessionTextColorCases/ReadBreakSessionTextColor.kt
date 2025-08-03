package com.umutsaydam.zenfocus.domain.usecases.local.cases.breakSessionTextColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadBreakSessionTextColor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readBreakSessionTextColorId()
    }
}