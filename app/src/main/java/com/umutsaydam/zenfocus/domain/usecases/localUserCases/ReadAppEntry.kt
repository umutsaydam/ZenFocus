package com.umutsaydam.zenfocus.domain.usecases.localUserCases

import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
    private val localUserManager: LocalUserManager
){
    operator fun invoke(): Flow<Boolean>{
        return localUserManager.readAppEntry()
    }
}
