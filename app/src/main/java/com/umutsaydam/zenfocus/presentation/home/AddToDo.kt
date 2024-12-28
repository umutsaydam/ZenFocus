package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.auth.FormTextField

@Composable
fun AddToDo(
    modifier: Modifier = Modifier,
    onClick: (TaskModel) -> Unit
) {
    var toDoTask by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PADDING_MEDIUM1),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1)
    ) {
        FormTextField(
            formTitle = stringResource(R.string.add_to_do),
            value = toDoTask,
            onValueChanged = { newTask ->
                toDoTask = newTask
            },
            placeHolder = stringResource(R.string.enter_your_task),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PADDING_MEDIUM2),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BUTTON_HEIGHT_MEDIUM),
                onClick = {
                    val newTask = TaskModel(taskContent = toDoTask)
                    onClick(newTask)
                    toDoTask = ""
                },
                enabled = toDoTask.isNotEmpty()
            ) {
                Text(
                    text = stringResource(R.string.select)
                )
            }
        }
    }
}