package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.exoplayer.ExoPlayer
import com.umutsaydam.zenfocus.domain.manager.ExoPlayerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class ExoPlayerManagerImpl(
    @ApplicationContext private val context: Context
) : ExoPlayerManager {
    private val _exoPlayer = MutableStateFlow<ExoPlayer?>(null)
    override val exoPlayer: StateFlow<ExoPlayer?> = _exoPlayer

    override fun initializePlayer() {
        if (_exoPlayer.value == null) {
            val player = ExoPlayer.Builder(context).build()
            player.repeatMode = REPEAT_MODE_ALL
            _exoPlayer.value = player
        }
    }

    override fun startPlayer(videoName: String, videoUrl: String?) {
        val mediaItem: MediaItem? = if (videoUrl != null) {
            getMediaItemFromUrl(videoUrl)
        } else {
            getMediaItemFromInternal(videoName)
        }

        mediaItem?.let {
            _exoPlayer.value?.apply {
                setMediaItem(it)
                prepare()
                play()
            }
        }
    }

    private fun getMediaItemFromUrl(videoUrl: String): MediaItem {
        return MediaItem.fromUri(videoUrl)
    }

    private fun getMediaItemFromInternal(videoName: String): MediaItem? {
        val file = File(context.filesDir, videoName)
        return if (file.exists()) {
            MediaItem.fromUri(file.absolutePath)
        } else {
            null
        }
    }

    override fun releasePlayer() {
        _exoPlayer.value?.release()
        _exoPlayer.value = null
    }

}