package com.umutsaydam.zenfocus.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL

@Composable
fun NumberPickerDialog(
    modifier: Modifier = Modifier,
    textTitle: String,
    gridState: LazyListState,
    range: IntRange = 0..94,
    content: @Composable (Int, Int) -> Unit,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var visibleIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(gridState) {
        snapshotFlow {
            gridState.firstVisibleItemIndex
        }.collect { index ->
            visibleIndex = index
        }
    }

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(CORNER_SMALL)
                )
                .size(300.dp)
                .padding(PADDING_MEDIUM1),
            verticalArrangement = Arrangement.spacedBy(SPACE_SMALL)
        ) {

            Text(
                text = textTitle
            )

            LazyColumn(
                modifier = Modifier
                    .padding(PADDING_MEDIUM1)
                    .weight(0.7f),
                verticalArrangement = Arrangement.Center,
                state = gridState
            ) {
                items(count = range.last) { currIndex ->
                    if (currIndex != range.first && currIndex + 3 < range.last) {
                        content(visibleIndex, currIndex)
                    } else {
                        Spacer(modifier = Modifier.height(SPACE_MEDIUM))
                    }
                }
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PADDING_SMALL)
                    .weight(0.3f),
                shape = RoundedCornerShape(CORNER_SMALL),
                onClick = {
                    onClick()
                },
//                enabled = toDoTask.isNotEmpty()
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
        }
    }
}