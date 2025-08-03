package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent

@Composable
fun <T> CenterFocusedCarouselListGeneric(
    modifier: Modifier = Modifier,
    listOfItems: List<T?>,
    gridState: LazyGridState,
    content: @Composable (Int, Int) -> Unit
) {
    var visibleIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(gridState) {
        snapshotFlow {
            gridState.firstVisibleItemIndex
        }.collect { index ->
            visibleIndex = index
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyHorizontalGrid(
            state = gridState,
            modifier = modifier,
            rows = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            items(listOfItems.size) { currentIndex ->
                content(visibleIndex, currentIndex)
            }
        }

        HorizontalLinearLinearGradient(
            modifier = Modifier
                .fillMaxHeight()
                .width(35.dp)
                .align(Alignment.CenterStart),
            colorList = listOf(
                SurfaceContainerLow,
                Transparent
            )
        )

        HorizontalLinearLinearGradient(
            modifier = Modifier
                .fillMaxHeight()
                .width(35.dp)
                .align(Alignment.CenterEnd),
            colorList = listOf(
                Transparent,
                SurfaceContainerLow
            )
        )
    }
}