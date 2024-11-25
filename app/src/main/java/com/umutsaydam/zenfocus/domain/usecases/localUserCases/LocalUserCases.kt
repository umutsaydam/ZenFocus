package com.umutsaydam.zenfocus.domain.usecases.localUserCases

data class LocalUserCases(
    val saveAppEntry: SaveAppEntry,
    val readAppEntry: ReadAppEntry,
    val saveVibrateState: SaveVibrateState,
    val readVibrateState: ReadVibrateState,
    val readAppLang: ReadAppLang,
    val saveAppLang: SaveAppLang
)
