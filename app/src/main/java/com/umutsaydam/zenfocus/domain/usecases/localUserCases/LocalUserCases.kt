package com.umutsaydam.zenfocus.domain.usecases.localUserCases

import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.DeleteUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.DeleteUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appEntryCases.ReadAppEntry
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appLangCases.ReadAppLang
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.ReadUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.ReadUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.vibrateCases.ReadVibrateState
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appEntryCases.SaveAppEntry
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appLangCases.SaveAppLang
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.vibrateCases.SaveVibrateState

data class LocalUserCases(
    val saveAppEntry: SaveAppEntry,
    val readAppEntry: ReadAppEntry,
    val saveVibrateState: SaveVibrateState,
    val readVibrateState: ReadVibrateState,
    val readAppLang: ReadAppLang,
    val saveAppLang: SaveAppLang,
    val readUserId: ReadUserId,
    val saveUserId: SaveUserId,
    val deleteUserId: DeleteUserId,
    val readUserType: ReadUserType,
    val saveUserType: SaveUserType,
    val deleteUserType: DeleteUserType
)
