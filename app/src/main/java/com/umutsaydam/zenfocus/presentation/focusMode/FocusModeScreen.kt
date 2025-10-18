package com.umutsaydam.zenfocus.presentation.focusMode

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.presentation.viewmodels.PomodoroUiState
import com.umutsaydam.zenfocus.presentation.viewmodels.PomodoroViewModel
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE2
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM2
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.focusMode.components.KeepScreenOn
import com.umutsaydam.zenfocus.presentation.common.CircularProgressWithText
import com.umutsaydam.zenfocus.presentation.viewmodels.FocusModeViewModel
import com.umutsaydam.zenfocus.presentation.viewmodels.VideoPlayerViewModel
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import kotlinx.coroutines.delay

@Composable
fun FocusModeScreen(
    navController: NavHostController,
    focusModeViewModel: FocusModeViewModel = hiltViewModel(),
    pomodoroViewModel: PomodoroViewModel = hiltViewModel(),
) {
    val currentProgressTrackColor by pomodoroViewModel.currentProgressTrackColor.collectAsState()
    val currentTextColor by pomodoroViewModel.currentTextColor.collectAsState()
    val defaultTheme by focusModeViewModel.defaultTheme.collectAsState()
    val pomodoroUiState by pomodoroViewModel.pomodoroUiState.collectAsState()
    var isThemeAnImage: Boolean? by remember { mutableStateOf(null) }

    LaunchedEffect(defaultTheme) {
        isThemeAnImage = focusModeViewModel.isThemeAnImage()
    }

    FocusLifeCycleHandler(
        navController = navController, viewModel = pomodoroViewModel
    )

    KeepScreenOn()
    StatusBarSwitcher(false)

    var alpha by remember { mutableFloatStateOf(1f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha, animationSpec = tween(durationMillis = 1000), label = "Focus Mode"
    )

    FocusModeAlphaHandler(alpha = alpha, onAlphaChange = { alpha = it })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            color = White, shape = RectangleShape
        )
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            alpha = 1f
        }) {
        isThemeAnImage?.let {
            if (it) {
                val rememberedBitmap by remember { derivedStateOf { defaultTheme?.asImageBitmap() } }
                FocusModeContentWithImage(rememberedBitmap = rememberedBitmap)
            } else {
                focusModeViewModel.themeName.value?.let {
                    val videoPlayerViewModel: VideoPlayerViewModel = hiltViewModel()
                    val videoPlayer by remember { derivedStateOf { videoPlayerViewModel.exoPlayer.value } }
                    LaunchedEffect(Unit) {
                        videoPlayerViewModel.startPlayer(it, null)
                    }
                    FocusModeContentWithVideo(videoPlayer = videoPlayer)
                }
            }
        }
    }

    if(currentProgressTrackColor != null && currentTextColor != null){
        PomodoroProgress(
            animatedAlpha = animatedAlpha,
            uiState = pomodoroUiState,
            progressColor = currentProgressTrackColor!!.color,
            textColor = currentTextColor!!.color
        )
    }
}

@Composable
fun FocusLifeCycleHandler(
    navController: NavHostController, viewModel: PomodoroViewModel
) {
    BackHandler(
        enabled = true
    ) {
        navController.popBackStackOrIgnore()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.setTimer()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun FocusModeAlphaHandler(
    alpha: Float, onAlphaChange: (Float) -> Unit
) {
    LaunchedEffect(alpha) {
        if (alpha == 1f) {
            delay(3000)
            onAlphaChange(0.5f)
        }
    }
}

@Composable
fun FocusModeContentWithImage(
    rememberedBitmap: ImageBitmap?
) {
    rememberedBitmap?.let { bitmap ->
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = bitmap,
            contentDescription = "Selected theme",
            contentScale = ContentScale.FillBounds
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun FocusModeContentWithVideo(
    videoPlayer: ExoPlayer?
) {
    val context = LocalContext.current
    if (videoPlayer != null) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = videoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                }
            }, modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PomodoroProgress(
    modifier: Modifier = Modifier,
    animatedAlpha: Float,
    uiState: PomodoroUiState,
    progressColor: Color,
    textColor: Color
) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressWithText(
            size = SIZE_LARGE2,
            animatedAlpha = animatedAlpha,
            progress = uiState.remainingPercent,
            isWorking = uiState.isWorkingSession,
            strokeWith = STROKE_MEDIUM2,
            progressColor = progressColor,
            strokeCap = StrokeCap.Round,
            text = uiState.remainingTime,
            textColor = textColor,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}