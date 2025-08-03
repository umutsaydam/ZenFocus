package com.umutsaydam.zenfocus.domain.usecases.local.cases.breakSessionTrackColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadBreakSessionTrackColor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readBreakSessionTrackColorId()
    }
}