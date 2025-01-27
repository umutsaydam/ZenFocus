package com.umutsaydam.zenfocus.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.ui.theme.Black

@Composable
fun CustomFab(
    modifier: Modifier = Modifier,
    alignment: Alignment,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = Black,
    fabIcon: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(PADDING_SMALL)
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