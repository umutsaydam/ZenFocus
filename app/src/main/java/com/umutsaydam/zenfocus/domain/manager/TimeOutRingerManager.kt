package com.umutsaydam.zenfocus.domain.manager

interface TimeOutRingerManager {
    fun playSound(soundResource: Int)

    fun stopSound()

    fun releaseMediaPlayer()

    fun deviceAudioModeAvailable(): Boolean
}