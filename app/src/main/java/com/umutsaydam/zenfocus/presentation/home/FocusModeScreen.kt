package com.umutsaydam.zenfocus.presentation.home

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE2
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM
import kotlinx.coroutines.delay

@Composable
fun FocusModeScreen(
    modifier: Modifier = Modifier
) {
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
                color = MaterialTheme.colorScheme.onBackground,
                shape = RectangleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                alpha = 1f
            }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.lofi1),
            contentDescription = "Selected theme",
            contentScale = ContentScale.Fit
        )
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressWithText(
                size = SIZE_LARGE2,
                animatedAlpha = animatedAlpha,
                progress = 0.6f,
                color = MaterialTheme.colorScheme.onBackground,
                strokeWith = STROKE_MEDIUM,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
                strokeCap = StrokeCap.Round,
                text = "25:00",
                textColor = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}