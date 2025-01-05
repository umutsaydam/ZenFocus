package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE1
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.OutLineVariant
import com.umutsaydam.zenfocus.ui.theme.White

@Composable
fun CircularProgressWithText(
    modifier: Modifier = Modifier,
    size: Dp = SIZE_LARGE1,
    animatedAlpha: Float = 1f,
    progress: Float,
    color: Color = Gray,
    strokeWith: Dp = STROKE_MEDIUM,
    trackColor: Color = OutLineVariant,
    strokeCap: StrokeCap = StrokeCap.Round,
    text: String,
    textColor: Color = White,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    durationMillis: Int = 1000,
    delayMillis: Int = 0
) {
    var animationPlayed by remember { mutableStateOf(false) }

    val currPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis
        ),
        label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    CircularProgressIndicator(
        modifier = modifier
            .size(size)
            .alpha(animatedAlpha),
        progress = {
            currPercentage.value
        },
        color = color,
        strokeWidth = strokeWith,
        trackColor = trackColor,
        strokeCap = strokeCap,
    )
    Text(
        text = text,
        color = textColor,
        style = style
    )
}