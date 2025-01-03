package com.umutsaydam.zenfocus.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.umutsaydam.zenfocus.presentation.Dimens.BORDER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL

@Composable
fun CustomTabButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    buttonText: String
) {
    val buttonShape = RoundedCornerShape(CORNER_SMALL)
    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = if (isSelected) MaterialTheme.colorScheme.background else Color.Transparent,
    )
    val borderStroke = BorderStroke(
        width = BORDER_SMALL,
        color = MaterialTheme.colorScheme.background
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
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    }
}