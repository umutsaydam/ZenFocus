package com.umutsaydam.zenfocus.presentation.policy.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.THICKNESS_SMALL
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Primary
import com.umutsaydam.zenfocus.ui.theme.Secondary

@Composable
fun RadioButtonWithText(
    modifier: Modifier = Modifier,
    radioSelected: Boolean,
    radioText: String,
    onClick: () -> Unit,
    radioColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Primary,
        unselectedColor = Secondary
    )
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(SPACE_SMALL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = radioSelected,
                onClick = { onClick() },
                colors = radioColors
            )

            Text(
                text = radioText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = if (radioSelected) Primary else Gray
                )
            )
        }

        HorizontalDivider(
            thickness = THICKNESS_SMALL,
            color = LightGray
        )
    }
}