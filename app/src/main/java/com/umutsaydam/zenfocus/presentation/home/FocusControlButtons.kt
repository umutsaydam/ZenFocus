package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_SMALL

@Composable
fun FocusControlButtons(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    contentColor: Color = Color.White,
    painterResource: Painter,
    contentDescription: String
) {
    IconButton(
        modifier = modifier
            .border(
                border = BorderStroke(width = STROKE_SMALL, color = Color.White),
                shape = RoundedCornerShape(CORNER_SMALL)
            ),
        onClick = {
            onClick()
        },
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Icon(
            painter = painterResource,
            contentDescription = contentDescription
        )
    }
}