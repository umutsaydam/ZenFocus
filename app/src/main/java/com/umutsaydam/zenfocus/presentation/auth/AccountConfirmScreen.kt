package com.umutsaydam.zenfocus.presentation.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL

@Composable
fun AccountConfirmScreen(
    modifier: Modifier = Modifier,
    email: String,
    navController: NavHostController,
    authConfirmViewModel: AuthConfirmViewModel = hiltViewModel()
) {
    var confirmCode by remember { mutableStateOf("") }
    Log.i("R/T", email)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormTextField(
            formTitle = stringResource(R.string.email),
            value = confirmCode,
            onValueChanged = {
                confirmCode = it
            },
            placeHolder = stringResource(R.string.enter_email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT_MEDIUM),
            onClick = {
                authConfirmViewModel.accountConfirm(email, confirmCode)
            },
            shape = RoundedCornerShape(CORNER_SMALL),
            enabled = confirmCode.length >= 4
        ) {
            Text(
                text = "Confirm"
            )
        }
    }
}