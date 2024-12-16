package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import kotlinx.coroutines.flow.StateFlow

interface FocusSoundUseCases {
    fun readSoundList(): Array<String>
    fun setSound(fileName: String)
    fun playSoundIfAvailable()
    fun isPlaying(): StateFlow<Boolean>
    fun stopSound()
}

class FocusSoundUseCasesImpl(
    private val focusSoundManager: FocusSoundManager
) : FocusSoundUseCases {
    override fun readSoundList(): Array<String> {
        return focusSoundManager.readSoundList()
    }

    override fun setSound(fileName: String) {
        focusSoundManager.setSound(fileName)
    }

    override fun playSoundIfAvailable() {
        focusSoundManager.playSoundIfAvailable()
    }

    override fun isPlaying(): StateFlow<Boolean> {
        return focusSoundManager.isPlaying
    }

    override fun stopSound() {
        focusSoundManager.stopSound()
    }
}