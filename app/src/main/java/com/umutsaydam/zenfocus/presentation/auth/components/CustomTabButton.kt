package com.umutsaydam.zenfocus.presentation.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umutsaydam.zenfocus.presentation.Dimens.BORDER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.Primary
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White

@Composable
fun CustomTabButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    buttonText: String
) {
    val buttonShape = RoundedCornerShape(CORNER_SMALL)
    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = if (isSelected) White else Transparent,
    )
    val borderStroke = BorderStroke(
        width = BORDER_SMALL,
        color = White
    ).takeIf { isSelected }

    OutlinedButton(
        onClick = {
            onClick()
        },
        shape = buttonShape,
        colors = buttonColors,
        border = borderStroke
    ) {
        Text(
            text = buttonText,
            color = if (isSelected) Primary else Outline
        )
    }
}