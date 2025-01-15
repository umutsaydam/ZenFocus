package com.umutsaydam.zenfocus.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.umutsaydam.zenfocus.presentation.Dimens.WEIGHT_LARGE
import com.umutsaydam.zenfocus.presentation.Dimens.WEIGHT_SMALL
import com.umutsaydam.zenfocus.ui.theme.Outline

@Composable
fun MenuItemDescription(
    modifier: Modifier = Modifier,
    menuTitle: String,
    description: String,
    textColor: Color = Outline,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        onClick = { onClick() },
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.weight(WEIGHT_LARGE),
                text = menuTitle,
                color = textColor,
            )

            Text(
                modifier = Modifier.weight(WEIGHT_SMALL),
                text = description,
                color = textColor,
            )
        }
    }
}