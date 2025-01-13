package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.manager.VibrationManager

interface VibrationManagerUseCases{
    fun setVibrateState(isEnabled: Boolean)
    fun vibrate(duration: Long)
}

class VibrationManagerUseCasesImpl(
    private val vibrationManager: VibrationManager
): VibrationManagerUseCases {
    override fun setVibrateState(isEnabled: Boolean) {
        vibrationManager.setVibrateState(isEnabled)
    }

    override fun vibrate(duration: Long) {
        vibrationManager.vibrate(duration)
    }
}