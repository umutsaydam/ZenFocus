package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.ui.theme.Outline

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoListItem(
    modifier: Modifier = Modifier,
    taskModel: TaskModel,
    textColor: Color = Outline,
    onClick: (Boolean) -> Unit,
    onLongClick: (TaskModel) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onClick(!taskModel.isTaskCompleted)
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
            checked = taskModel.isTaskCompleted,
            onCheckedChange = { newState ->
                onClick(newState)
            }
        )

        Text(
            modifier = Modifier.weight(0.8f),
            text = taskModel.taskContent,
            color = textColor,
            style = TextStyle(
                fontStyle = if (taskModel.isTaskCompleted) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (taskModel.isTaskCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
        )
    }
}