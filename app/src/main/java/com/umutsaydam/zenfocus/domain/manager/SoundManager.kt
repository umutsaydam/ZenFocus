package com.umutsaydam.zenfocus.domain.manager

interface SoundManager {
    fun playSound(soundResource: Int)

    fun stopSound()

    fun releaseMediaPlayer()

    fun deviceAudioModeAvailable(): Boolean
}