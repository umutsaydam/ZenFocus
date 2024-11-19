package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL

@Composable
fun CustomFab(
    modifier: Modifier = Modifier,
    alignment: Alignment,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = Color.Black,
    fabIcon: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM1)
    ) {
        FloatingActionButton(
            modifier = Modifier
                .align(alignment)
                .padding(PADDING_SMALL),
            onClick = {
                onClick()
            },
            shape = RoundedCornerShape(CORNER_MEDIUM),
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            Icon(
                painter = fabIcon,
                contentDescription = contentDescription,
            )
        }
    }
}