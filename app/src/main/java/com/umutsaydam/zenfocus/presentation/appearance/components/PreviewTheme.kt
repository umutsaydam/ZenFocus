package com.umutsaydam.zenfocus.presentation.appearance.components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import coil3.compose.rememberAsyncImagePainter
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo
import com.umutsaydam.zenfocus.presentation.viewmodels.VideoPlayerViewModel

@Composable
fun PreviewTheme(
    selectedTheme: ThemeInfo
) {
    Image(
        modifier = Modifier
            .fillMaxSize(0.7f)
            .clip(RoundedCornerShape(16.dp)),
        painter = rememberAsyncImagePainter(selectedTheme.themeUrl),
        contentDescription = "Selected Theme",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PreviewThemeWithProgressBar(
    selectedTheme: Bitmap
) {
    Image(
        modifier = Modifier
            .fillMaxSize(0.7f)
            .clip(RoundedCornerShape(16.dp)),
        painter = rememberAsyncImagePainter(selectedTheme),
        contentDescription = "Selected Theme",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PreviewTheme(
    selectedTheme: ThemeInfo,
    context: Context,
    videoPlayerViewModel: VideoPlayerViewModel
) {
    val videoPlayer by videoPlayerViewModel.exoPlayer.collectAsState()

    LaunchedEffect(selectedTheme.themeName) {
        videoPlayerViewModel.startPlayer(selectedTheme.themeName, selectedTheme.themeUrl)
    }

    videoPlayer?.let { exoPlayer ->
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxSize(0.7f)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun PreviewThemeWithProgressBar(
    selectedThemeName: String,
    context: Context,
    videoPlayerViewModel: VideoPlayerViewModel
) {
    val videoPlayer by videoPlayerViewModel.exoPlayer.collectAsState()

    LaunchedEffect(selectedThemeName) {
        videoPlayerViewModel.startPlayer(selectedThemeName, null)
    }

    videoPlayer?.let { exoPlayer ->
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxSize(0.7f)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}