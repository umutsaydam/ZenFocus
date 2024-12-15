package com.umutsaydam.zenfocus.domain.manager

interface VibrationManager {
    fun vibrate(duration: Long)

    fun deviceVibrateModeAvailable(): Boolean
}