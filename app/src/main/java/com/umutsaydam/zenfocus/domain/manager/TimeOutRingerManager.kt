package com.umutsaydam.zenfocus.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface TimeOutRingerManager {
    val isRingerEnabled: StateFlow<Boolean>

    fun isRingerEnabled(isEnabled: Boolean)
    fun playSound(soundResource: Int)
    fun stopSound()
    fun releaseMediaPlayer()
    fun deviceAudioModeAvailable(): Boolean
}