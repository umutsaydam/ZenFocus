package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.umutsaydam.zenfocus.R

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    text: String,
    isConfirmed: (Boolean) -> Unit
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                painter = icon,
                contentDescription = "Desc",
                tint = Color.Gray
            )
        },
        title = {
            Text(
                title
            )
        },
        text = {
            Text(
                text,
                color = Color.Gray
            )
        },
        onDismissRequest = { isConfirmed(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    isConfirmed(true)
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isConfirmed(false)
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel)
                )
            }
        },
        shape = AlertDialogDefaults.shape
    )
}