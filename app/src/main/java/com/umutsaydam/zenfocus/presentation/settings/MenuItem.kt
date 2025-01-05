package com.umutsaydam.zenfocus.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.THICKNESS_SMALL
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Outline

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    headIcon: Int? = null,
    contentDescription: String = "",
    iconTint: Color = Outline,
    menuTitle: String,
    textColor: Color = Outline,
    onClick: () -> Unit,
    isEnable: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() },
            enabled = isEnable,
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (headIcon != null) {
                    Icon(
                        painter = painterResource(headIcon),
                        contentDescription = contentDescription,
                        tint = iconTint
                    )
                    Spacer(modifier = Modifier.width(SPACE_SMALL))
                }
                Text(
                    text = menuTitle,
                    color = textColor,
                )
            }
        }
        HorizontalDivider(
            thickness = THICKNESS_SMALL,
            color = LightGray
        )
    }
}