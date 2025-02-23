package com.umutsaydam.zenfocus.domain.manager

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.StateFlow

interface ExoPlayerManager {
    val exoPlayer: StateFlow<ExoPlayer?>
    fun initializePlayer()
    fun startPlayer(videoName: String, videoUrl: String?)
    fun releasePlayer()
}