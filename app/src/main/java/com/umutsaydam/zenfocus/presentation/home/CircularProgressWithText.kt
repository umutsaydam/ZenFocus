package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE1
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM

@Composable
fun CircularProgressWithText(
    modifier: Modifier = Modifier,
    size: Dp = SIZE_LARGE1,
    animatedAlpha: Float = 1f,
    progress: Float,
    color: Color = MaterialTheme.colorScheme.onBackground,
    strokeWith: Dp = STROKE_MEDIUM,
    trackColor: Color = MaterialTheme.colorScheme.outlineVariant,
    strokeCap: StrokeCap = StrokeCap.Round,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.background,
    style: TextStyle = MaterialTheme.typography.titleMedium
) {
    CircularProgressIndicator(
        modifier = modifier
            .size(size)
            .alpha(animatedAlpha),
        progress = {
            progress
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