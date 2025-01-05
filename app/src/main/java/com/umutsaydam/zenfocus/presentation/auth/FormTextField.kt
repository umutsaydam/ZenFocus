package com.umutsaydam.zenfocus.presentation.auth

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.ui.theme.Black
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.Transparent

@Composable
fun FormTextField(
    formTitle: String,
    value: String,
    onValueChanged: (String) -> Unit,
    placeHolder: String,
    keyboardOptions: KeyboardOptions,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Black,
        unfocusedTextColor = Black,
        focusedIndicatorColor = Transparent,
        unfocusedIndicatorColor = Transparent,
        focusedContainerColor = Transparent,
        unfocusedContainerColor = Transparent
    )
) {
    Text(
        text = formTitle,
        color = Black
    )
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Gray,
                shape = RoundedCornerShape(CORNER_SMALL)
            ),
        value = value,
        onValueChange = { onValueChanged(it) },
        placeholder = {
            Text(
                text = placeHolder,
                color = Outline
            )
        },
        visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(CORNER_SMALL),
        colors = colors
    )
}


@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun FormTextFieldPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(all = 50.dp)
    ) {
        FormTextField(
            formTitle = "Email",
            value = "",
            onValueChanged = {},
            placeHolder = "",
            keyboardOptions = KeyboardOptions()
        )
    }
}