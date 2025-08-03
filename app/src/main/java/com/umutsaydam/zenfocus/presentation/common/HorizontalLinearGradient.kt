package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

@Composable
fun HorizontalLinearLinearGradient(
    modifier: Modifier = Modifier,
    colorList: List<Color>,
    start: Float = 0f
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = colorList,
                    startX = start
                )
            )
            .zIndex(1f)
    )
}