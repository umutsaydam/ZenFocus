package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.manager.TimeOutRingerManager

interface TimeOutRingerManagerUseCases{
    fun isRingerEnabled(isEnabled: Boolean)
    fun playSound(soundResource: Int)
    fun stopSound()
}

class TimeOutRingerManagerUseCasesImpl(
    private val timeOutRingerManager : TimeOutRingerManager
): TimeOutRingerManagerUseCases {
    override fun isRingerEnabled(isEnabled: Boolean) {
        timeOutRingerManager.isRingerEnabled(isEnabled)
    }

    override fun playSound(soundResource: Int) {
        timeOutRingerManager.playSound(soundResource)
    }

    override fun stopSound() {
        timeOutRingerManager.stopSound()
    }
}