package com.umutsaydam.zenfocus.presentation.confirmAccount

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.auth.FormTextField
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun AccountConfirmScreen(
    modifier: Modifier = Modifier,
    email: String,
    shouldResend: Boolean,
    navController: NavHostController,
    authConfirmViewModel: AuthConfirmViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val networkErrorMessage = stringResource(R.string.no_connection)
    var confirmCode by remember { mutableStateOf("") }
    val userConfirmState by authConfirmViewModel.userConfirmState.collectAsState()
    val uiMessage by authConfirmViewModel.uiMessage.collectAsState()
    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            authConfirmViewModel.clearUiMessage()
        }
    }

    LaunchedEffect(userConfirmState) {
        if (userConfirmState == AuthSignUpStep.DONE) {
            navController.popBackStackOrIgnore()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (shouldResend) {
            authConfirmViewModel.resendConfirmationCode(email)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceContainerLow)
            .padding(PADDING_MEDIUM2),
        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormTextField(
            formTitle = stringResource(R.string.sent_code_confirm_account),
            value = confirmCode,
            onValueChanged = {
                confirmCode = it
            },
            placeHolder = stringResource(R.string.enter_confirm_code),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT_MEDIUM),
            onClick = {
                if (authConfirmViewModel.isConnected()) {
                    authConfirmViewModel.accountConfirm(email, confirmCode)
                } else {
                    Toast.makeText(context, networkErrorMessage, Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(CORNER_SMALL),
            enabled = confirmCode.length >= 6
        ) {
            Text(
                text = stringResource(R.string.confirm)
            )
        }
    }
}