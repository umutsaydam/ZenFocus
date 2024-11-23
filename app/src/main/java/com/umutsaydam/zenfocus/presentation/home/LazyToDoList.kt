package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1

@Composable
fun LazyToDoList(
    modifier: Modifier = Modifier,
    toDoList: List<String>,
    content: @Composable (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(PADDING_MEDIUM1)
            .clip(RoundedCornerShape(CORNER_SMALL))
            .fillMaxWidth()
            .background(Color.LightGray),
    ) {
        items(count = toDoList.size, key = { it }) { index ->
            content(index)
        }
    }
}