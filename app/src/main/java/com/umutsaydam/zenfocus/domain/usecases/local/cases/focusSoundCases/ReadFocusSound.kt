package com.umutsaydam.zenfocus.domain.usecases.local.cases.focusSoundCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadFocusSound(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readFocusSound()
    }
}