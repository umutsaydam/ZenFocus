package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.umutsaydam.zenfocus.domain.usecases.local.ExoPlayerManagerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val exoPlayerManagerUseCases: ExoPlayerManagerUseCases
) : ViewModel() {

    init {
        exoPlayerManagerUseCases.initializePlayer()
    }

    fun startPlayer(videoName: String, videoUrl: String?) {
        exoPlayerManagerUseCases.startPlayer(videoName, videoUrl)
    }

    val exoPlayer = exoPlayerManagerUseCases.getPlayer()

    private fun releasePlayer() {
        exoPlayerManagerUseCases.releasePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}