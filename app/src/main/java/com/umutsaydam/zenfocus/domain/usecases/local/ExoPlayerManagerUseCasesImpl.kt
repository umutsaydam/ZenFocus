package com.umutsaydam.zenfocus.domain.usecases.local

import androidx.media3.exoplayer.ExoPlayer
import com.umutsaydam.zenfocus.domain.manager.ExoPlayerManager
import kotlinx.coroutines.flow.StateFlow

interface ExoPlayerManagerUseCases {
    fun initializePlayer()
    fun getPlayer(): StateFlow<ExoPlayer?>
    fun startPlayer(videoName: String, videoUrl: String?)
    fun releasePlayer()
}

class ExoPlayerManagerUseCasesImpl(
    private val exoPlayerManager: ExoPlayerManager
) : ExoPlayerManagerUseCases {
    override fun initializePlayer() {
        exoPlayerManager.initializePlayer()
    }

    override fun getPlayer(): StateFlow<ExoPlayer?> {
        return exoPlayerManager.exoPlayer
    }

    override fun startPlayer(videoName: String, videoUrl: String?) {
        exoPlayerManager.startPlayer(videoName, videoUrl)
    }

    override fun releasePlayer() {
        exoPlayerManager.releasePlayer()
    }
}