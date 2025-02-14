package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import android.content.res.AssetManager
import android.media.MediaPlayer
import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import com.umutsaydam.zenfocus.util.Constants.NONE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FocusSoundManagerImpl(
    @ApplicationContext private val context: Context
) : FocusSoundManager {
    private val _currentSoundName = MutableStateFlow<String>(NONE)
    override val currentSoundName: StateFlow<String> = _currentSoundName

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying

    private val assetManager: AssetManager by lazy {
        context.assets
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun readSoundList(): Array<String> {
        val soundList = assetManager.list("sounds") ?: emptyArray<String>()
        return arrayOf(NONE) + soundList
    }

    override fun setSound(fileName: String) {
        _currentSoundName.value = fileName
    }

    override fun playSoundIfAvailable() {
        try {
            if (_isPlaying.value || _currentSoundName.value == NONE) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
            }
            val descriptor = assetManager.openFd("sounds/${_currentSoundName.value}")
            mediaPlayer = MediaPlayer()

            mediaPlayer?.let {
                it.setDataSource(
                    descriptor.fileDescriptor, descriptor.startOffset, descriptor.length
                )
                descriptor.close()
                it.prepare()
                it.isLooping = true
                it.start()
                _isPlaying.value = true
            }
        } catch (e: Exception) {
            _isPlaying.value = false
        }
    }

    override fun stopSound() {
        if (_isPlaying.value) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            _isPlaying.value = false
        }
    }
}