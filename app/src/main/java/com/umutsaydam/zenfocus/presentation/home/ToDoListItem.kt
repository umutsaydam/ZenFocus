package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

@Composable
fun ToDoListItem(
    modifier: Modifier = Modifier,
    toDoTitle: String,
    textColor: Color = MaterialTheme.colorScheme.outline,
    onClick: () -> Unit,
    isChecked: Boolean = false
) {
    var checkState by remember {
        mutableStateOf(isChecked)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            modifier = Modifier
                .scale(0.7f)
                .weight(0.2f),
            checked = checkState,
            onCheckedChange = { newState ->
                checkState = newState
                onClick()
            }
        )

        Text(
            modifier = Modifier.weight(0.8f),
            text = toDoTitle,
            color = textColor,
        )
    }
}