package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import android.media.MediaPlayer
import com.umutsaydam.zenfocus.domain.manager.TimeOutRingerManager
import com.umutsaydam.zenfocus.domain.model.RingerModeEnum
import com.umutsaydam.zenfocus.domain.usecases.local.DeviceRingerModeCases
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimeOutRingerManagerImpl(
    private val ringerModeCases: DeviceRingerModeCases,
    @ApplicationContext private val context: Context
) : TimeOutRingerManager {
    private val _isRingerEnabled = MutableStateFlow(true)
    override val isRingerEnabled: StateFlow<Boolean> = _isRingerEnabled
    private var mediaPlayer: MediaPlayer? = null

    override fun isRingerEnabled(isEnabled: Boolean) {
        _isRingerEnabled.value = isEnabled
    }

    override fun playSound(soundResource: Int) {
        if (_isRingerEnabled.value && deviceAudioModeAvailable()) {
            mediaPlayer = MediaPlayer.create(context, soundResource).apply {
                setOnCompletionListener {
                    releaseMediaPlayer()
                }
                start()
            }
        }
    }

    override fun stopSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            releaseMediaPlayer()
        }
    }

    override fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun deviceAudioModeAvailable(): Boolean {
        return ringerModeCases.readRingerMode() == RingerModeEnum.NORMAL
    }
}