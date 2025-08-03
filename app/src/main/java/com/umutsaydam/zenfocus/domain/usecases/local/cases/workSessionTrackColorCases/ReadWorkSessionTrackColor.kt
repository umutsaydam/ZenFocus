package com.umutsaydam.zenfocus.domain.usecases.local.cases.workSessionTrackColorCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadWorkSessionTrackColor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Int> {
        return localUserManager.readWorkSessionTrackColorId()
    }
}