package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.umutsaydam.zenfocus.domain.model.TaskModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoListItem(
    modifier: Modifier = Modifier,
    taskModel: TaskModel,
    textColor: Color = MaterialTheme.colorScheme.outline,
    onClick: (Boolean) -> Unit,
    onLongClick: (TaskModel) -> Unit
) {
    var checkState by remember {
        mutableStateOf(taskModel.isTaskCompleted)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    checkState = !checkState
                    onClick(checkState)
                },
                onLongClick = {
                    onLongClick(taskModel)
                }
            ),
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
                onClick(newState)
            }
        )

        Text(
            modifier = Modifier.weight(0.8f),
            text = taskModel.taskContent,
            color = textColor,
            style = TextStyle(
                fontStyle = if (checkState) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (checkState) TextDecoration.LineThrough else TextDecoration.None
            )
        )
    }
}