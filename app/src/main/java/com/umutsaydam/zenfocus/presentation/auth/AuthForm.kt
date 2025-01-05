package com.umutsaydam.zenfocus.presentation.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.ui.theme.Black
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.White

@Composable
fun AuthForm(
    authEmail: String,
    authPassword: String,
    buttonText: String,
    onClick: () -> Unit
) {
    var email by remember { mutableStateOf(authEmail) }
    var password by remember { mutableStateOf(authPassword) }

    FormTextField(
        formTitle = stringResource(R.string.email),
        value = email,
        onValueChanged = {
            email = it
        },
        placeHolder = stringResource(R.string.enter_email),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
    FormTextField(
        formTitle = stringResource(R.string.password),
        value = password,
        onValueChanged = {
            password = it
        },
        placeHolder = stringResource(R.string.enter_password),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT_MEDIUM),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black,
            disabledContainerColor = LightGray,
            disabledContentColor = Gray
        ),
        onClick = {
            email = email.trim()
            password = password.trim()
            onClick()
        },
        shape = RoundedCornerShape(CORNER_SMALL),
        enabled = email.isNotEmpty() && password.isNotEmpty()
    ) {
        Text(
            text = buttonText
        )
    }
}