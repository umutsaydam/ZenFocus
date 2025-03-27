package com.umutsaydam.zenfocus.presentation.statistics

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.umutsaydam.zenfocus.ui.theme.OutLineVariant
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SemiLightBlue
import com.umutsaydam.zenfocus.ui.theme.White

@Composable
fun SingleChoiceSegmentedButtons(
    rememberScrollState: ScrollState,
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChanged: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState)
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                modifier = Modifier
                    .wrapContentWidth(),
                selected = selectedIndex == index,
                onClick = {
                    onSelectedIndexChanged(index)
                },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                colors = SegmentedButtonDefaults.colors().copy(
                    activeContainerColor = SemiLightBlue,
                    activeContentColor = White,
                    activeBorderColor = OutLineVariant,
                    inactiveContainerColor = White,
                    inactiveContentColor = Outline,
                    inactiveBorderColor = OutLineVariant,
                )
            )
        }
    }
}