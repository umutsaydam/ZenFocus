package com.umutsaydam.zenfocus.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface VibrationManager {
    val isVibrateEnabled: StateFlow<Boolean>

    fun setVibrateState(isEnabled: Boolean)
    fun vibrate(duration: Long)
    fun deviceVibrateModeAvailable(): Boolean
}