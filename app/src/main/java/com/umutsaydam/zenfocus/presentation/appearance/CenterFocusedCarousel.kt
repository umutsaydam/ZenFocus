package com.umutsaydam.zenfocus.presentation.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CenterFocusedCarousel(
    modifier: Modifier = Modifier,
    listOfTheme: List<Int?>,
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

    Text(text = "First Visible Index: $visibleIndex")

    LazyHorizontalGrid(
        state = gridState,
        modifier = modifier
            .width(300.dp)
            .background(Color.Yellow),
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listOfTheme.size) { currentIndex ->
            content(visibleIndex, currentIndex)
        }
    }
}