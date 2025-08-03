package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umutsaydam.zenfocus.util.Constants.SELECTING_ANIM_DUR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun <T> CenterFocusedCarouselGeneric(
    modifier: Modifier = Modifier,
    items: List<T?>,
    gridState: LazyGridState,
    isTablet: Boolean,
    themeSpace: Dp,
    coroutineScope: CoroutineScope,
    onItemSelected: (T) -> Unit,
    content: @Composable (
        item: T?,
        isFocused: Boolean,
        animatedWidth: Dp,
        animatedHeight: Dp,
        onClick: () -> Job
    ) -> Unit
) {
    CenterFocusedCarouselListGeneric(
        modifier = modifier,
        listOfItems = items,
        gridState = gridState,
        content = { firstVisibleIndex, currentIndex ->
            val item = items[currentIndex]
            if (item != null) {
                val isFocused = firstVisibleIndex + 1 == currentIndex
                val (imageWidth, imageHeight) = calculateImageSize(isTablet, isFocused)

                val animatedWidth by animateDpAsState(
                    targetValue = imageWidth,
                    animationSpec = tween(
                        durationMillis = SELECTING_ANIM_DUR,
                        easing = LinearEasing
                    ),
                    label = "Animated Width"
                )

                val animatedHeight by animateDpAsState(
                    targetValue = imageHeight,
                    animationSpec = tween(
                        durationMillis = SELECTING_ANIM_DUR,
                        easing = LinearEasing
                    ),
                    label = "Animated Height"
                )

                val onClick = {
                    coroutineScope.launch {
                        gridState.animateScrollToItem(index = max(0, currentIndex - 1))
                    }
                }

                content(item, isFocused, animatedWidth, animatedHeight, onClick)

                if (isFocused) {
                    onItemSelected(item)
                }
            } else {
                Spacer(modifier = Modifier.width(themeSpace))
            }
        }
    )
}

private fun calculateImageSize(isTablet: Boolean, isBigger: Boolean): Pair<Dp, Dp> {
    return if (isTablet) {
        if (isBigger) 180.dp to 190.dp else 170.dp to 180.dp
    } else {
        if (isBigger) 100.dp to 110.dp else 80.dp to 90.dp
    }
}