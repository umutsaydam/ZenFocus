package com.umutsaydam.zenfocus.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.umutsaydam.zenfocus.presentation.Dimens.THICKNESS_SMALL
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.Primary

@Composable
fun MenuItemSwitch(
    modifier: Modifier = Modifier,
    menuTitle: String,
    textColor: Color = Outline,
    onClick: (Boolean) -> Unit,
    isChecked: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TextButton(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                onClick(!isChecked)
            },
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier.weight(0.8f),
                    text = menuTitle,
                    color = textColor,
                )

                Switch(
                    modifier = Modifier
                        .scale(0.7f)
                        .weight(0.2f),
                    checked = isChecked,
                    onCheckedChange = { newState ->
                        onClick(newState)
                    },
                    colors = SwitchDefaults.colors().copy(
                        checkedThumbColor = Primary,
                        checkedTrackColor = LightGray,
                        uncheckedThumbColor = LightGray,
                        uncheckedTrackColor = Primary,
                    )
                )
            }
        }
        HorizontalDivider(
            thickness = THICKNESS_SMALL,
            color = LightGray
        )
    }
}