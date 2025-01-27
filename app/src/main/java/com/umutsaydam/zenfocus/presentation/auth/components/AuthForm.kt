package com.umutsaydam.zenfocus.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    buttonText: String,
    onClick: () -> Unit
) {
    FormTextField(
        formTitle = stringResource(R.string.email),
        value = authEmail,
        onValueChanged = onEmailChange,
        placeHolder = stringResource(R.string.enter_email),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
    FormTextField(
        formTitle = stringResource(R.string.password),
        value = authPassword,
        onValueChanged = onPasswordChange,
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
            onClick()
        },
        shape = RoundedCornerShape(CORNER_SMALL),
        enabled = authEmail.isNotEmpty() && authPassword.isNotEmpty()
    ) {
        Text(
            text = buttonText
        )
    }
}
