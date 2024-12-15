package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.DeleteUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.DeleteUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.ReadAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.ReadAppLang
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.ReadUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.ReadUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.ReadVibrateState
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.SaveAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.SaveAppLang
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases.ReadPomodoroBreakDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases.SavePomodoroBreakDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases.ReadPomodoroCycle
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases.SavePomodoroCycle
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases.ReadPomodoroWorkDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases.SavePomodoroWorkDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.themeCases.ReadTheme
import com.umutsaydam.zenfocus.domain.usecases.local.cases.themeCases.SaveTheme
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.SaveVibrateState

data class LocalUserDataStoreCases(
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
    val deleteUserType: DeleteUserType,
    val saveTheme: SaveTheme,
    val readTheme: ReadTheme,
    val savePomodoroBreakDuration: SavePomodoroBreakDuration,
    val readPomodoroBreakDuration: ReadPomodoroBreakDuration,
    val savePomodoroCycle: SavePomodoroCycle,
    val readPomodoroCycle: ReadPomodoroCycle,
    val savePomodoroWorkDuration: SavePomodoroWorkDuration,
    val readPomodoroWorkDuration: ReadPomodoroWorkDuration
)
