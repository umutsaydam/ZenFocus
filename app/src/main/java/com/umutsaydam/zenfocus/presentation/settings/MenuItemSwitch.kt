package com.umutsaydam.zenfocus.presentation.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.umutsaydam.zenfocus.ui.theme.Outline

@Composable
fun MenuItemSwitch(
    modifier: Modifier = Modifier,
    menuTitle: String,
    textColor: Color = Outline,
    onClick: (Boolean) -> Unit,
    isChecked: Boolean = true
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

                )
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MenuItemSwitchPreview(modifier: Modifier = Modifier) {
    MenuItemSwitch(
        menuTitle = "Vibrate",
        onClick = {

        },
    )
}