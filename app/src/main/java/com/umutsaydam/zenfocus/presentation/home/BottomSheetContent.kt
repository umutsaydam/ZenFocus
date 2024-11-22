package com.umutsaydam.zenfocus.presentation.home

sealed class BottomSheetContent {
    data object PomodoroTimes : BottomSheetContent()
    data object PomodoroSounds : BottomSheetContent()
    data object AddToDo : BottomSheetContent()
}