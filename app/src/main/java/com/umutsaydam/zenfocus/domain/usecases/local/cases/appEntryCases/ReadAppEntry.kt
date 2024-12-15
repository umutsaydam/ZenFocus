package com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases

import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
    private val localUserManager: LocalUserManager
){
    operator fun invoke(): Flow<Boolean>{
        return localUserManager.readAppEntry()
    }
}
