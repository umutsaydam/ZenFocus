package com.umutsaydam.zenfocus.presentation.focusMode

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE2
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.home.CircularProgressWithText
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import kotlinx.coroutines.delay

@Composable
fun FocusModeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    focusModeViewModel: FocusModeViewModel = hiltViewModel()
) {
    val defaultTheme by focusModeViewModel.defaultTheme.collectAsState()
    val uiState by focusModeViewModel.uiState.collectAsState()
    var isBackPressed by remember { mutableStateOf(false) }

    BackHandler(
        enabled = true
    ) {
        isBackPressed = true
        navController.popBackStackOrIgnore()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    focusModeViewModel.setTimer()
                }

                Lifecycle.Event.ON_STOP -> {
                    if (!isBackPressed) {
                        focusModeViewModel.startPomodoroService()
                    }
                    isBackPressed = false
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    KeepScreenOn()
    StatusBarSwitcher(false)

    var alpha by remember { mutableFloatStateOf(1f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1000),
        label = "Focus Mode"
    )

    LaunchedEffect(alpha) {
        if (alpha == 1f) {
            delay(3000)
            alpha = 0.5f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = White,
                shape = RectangleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                alpha = 1f
            }
    ) {
        defaultTheme?.let {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected theme",
                contentScale = ContentScale.FillBounds
            )
        }
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressWithText(
                size = SIZE_LARGE2,
                animatedAlpha = animatedAlpha,
                progress = uiState.remainingPercent,
                strokeWith = STROKE_MEDIUM,
                strokeCap = StrokeCap.Round,
                text = uiState.remainingTime,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}