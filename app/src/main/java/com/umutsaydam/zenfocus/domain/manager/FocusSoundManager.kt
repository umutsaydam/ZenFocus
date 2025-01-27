package com.umutsaydam.zenfocus.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface FocusSoundManager {
    val currentSoundName: StateFlow<String>
    val isPlaying: StateFlow<Boolean>
    fun readSoundList(): Array<String>
    fun setSound(fileName: String)
    fun playSoundIfAvailable()
    fun stopSound()
}