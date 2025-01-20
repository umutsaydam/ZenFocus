package com.umutsaydam.zenfocus.presentation.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.ui.theme.Black
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val currPage = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.uiMessage, uiState.signInStep, uiState.signUpStep) {
        uiState.uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            authViewModel.clearUiMessage()
        }

        when (uiState.signInStep) {
            AuthSignInStep.DONE -> {
                navController.popBackStackOrIgnore()
            }

            AuthSignInStep.CONFIRM_SIGN_UP -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.confirm_account),
                    Toast.LENGTH_SHORT
                ).show()
                val confirmRoute = "AccountConfirm/$email/true"
                authViewModel.clearAuthSignInStep()
                navController.safeNavigate(confirmRoute)
            }

            else -> {}
        }

        when (uiState.signUpStep) {
            AuthSignUpStep.CONFIRM_SIGN_UP_STEP -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.confirm_account),
                    Toast.LENGTH_SHORT
                ).show()
                val confirmRoute = "AccountConfirm/$email/false"
                authViewModel.clearAuthSignUpStep()
                navController.safeNavigate(confirmRoute)
            }

            AuthSignUpStep.DONE -> {
                Toast.makeText(context, context.getString(R.string.signed_up), Toast.LENGTH_SHORT)
                    .show()
            }

            AuthSignUpStep.COMPLETE_AUTO_SIGN_IN -> {
            }

            null -> {
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackOrIgnore()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = Outline
                        )
                    }
                },
                containerColor = SurfaceContainerLow
            )
        }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceContainerLow)
                .padding(
                    top = topPadding,
                    start = PADDING_MEDIUM2,
                    end = PADDING_MEDIUM2
                )
                .imePadding()
                .imeNestedScroll(),
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM),
        ) {
            item { Spacer(modifier = Modifier.height(SPACE_SMALL)) }

            item {
                AuthSection {
                    Text(
                        text = stringResource(R.string.welcome),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Black
                        )
                    )
                    Text(
                        text = stringResource(R.string.login_to_access_account),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Outline
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(CORNER_SMALL))
                            .background(SurfaceContainerLow)
                            .padding(horizontal = PADDING_SMALL),
                    ) {
                        listOf("sign_in" to 0, "sign_up" to 1).forEach { (text, index) ->
                            CustomTabButton(
                                isSelected = currPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(index)
                                    }
                                },
                                buttonText = stringResource(if (text == "sign_in") R.string.sign_in else R.string.sign_up)
                            )
                        }
                    }
                }
            }

            item {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    AuthSection(
                        modifier = Modifier.padding(horizontal = PADDING_SMALL),
                        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1),
                        content = {
                            val isSignInSection = page == 0
                            AuthForm(
                                authEmail = email,
                                authPassword = password,
                                onEmailChange = { email = it },
                                onPasswordChange = { password = it },
                                buttonText = stringResource(if (isSignInSection) R.string.sign_in else R.string.sign_up),
                                onClick = {
                                    if (authViewModel.isConnected()) {
                                        if (isSignInSection) {
//                                            These line is commented for the open source contribution.
//                                            authViewModel.signIn(email, password)
                                        } else {
                                            if (password.length > 7) {
//                                                These line is commented for the open source contribution.
//                                                authViewModel.signUp(email, password)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.password_length),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.no_connection),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(if (currPage == 0) R.string.or_sign_in_with else R.string.or_sign_up_with),
                    textAlign = TextAlign.Center,
                    color = Outline
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = PADDING_SMALL),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        modifier = Modifier,
                        onClick = {
                            activity?.let {
                                if (authViewModel.isConnected()) {
//                                    These line is commented for the open source contribution.
//                                    authViewModel.signInWithGoogle(it)
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.no_connection),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        shape = RoundedCornerShape(CORNER_SMALL),
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = White,
                            contentColor = Black
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .size(SIZE_MEDIUM2)
                                .padding(horizontal = PADDING_SMALL),
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = stringResource(R.string.join_with_google)
                        )
                        Text(
                            text = stringResource(R.string.google),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}