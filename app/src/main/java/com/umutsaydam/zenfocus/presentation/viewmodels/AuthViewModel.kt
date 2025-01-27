package com.umutsaydam.zenfocus.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val signUpStep: AuthSignUpStep? = null,
    val signInStep: AuthSignInStep? = null,
    val userId: String? = null,
    val uiMessage: Int? = null,

    )

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val awsAuthCases: AwsAuthCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val networkCheckerUseCases: NetworkCheckerUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private fun updateUiState(update: AuthUiState.() -> AuthUiState) {
        _uiState.value = _uiState.value.update()
    }

    fun isConnected(): Boolean {
        return networkCheckerUseCases.isConnected()
    }

//    fun signIn(email: String, password: String) {
//        viewModelScope.launch {
//            when (val result: AwsAuthSignInResult = awsAuthCases.userSignIn(email, password)) {
//                is AwsAuthSignInResult.IsSignedIn -> {
//                    updateUiState { copy(uiMessage = R.string.signing_in) }
//                    getUserId()
//                }
//
//                is AwsAuthSignInResult.ConfirmSignIn -> {
//                    updateUiState {
//                        copy(
//                            userId = null,
//                            signUpStep = AuthSignUpStep.CONFIRM_SIGN_UP_STEP
//                        )
//                    }
//                }
//
//                is AwsAuthSignInResult.Error -> {
//                    updateUiState { copy(userId = null, uiMessage = result.message) }
//
//                    when (result.exception) {
//                        is UserNotConfirmedException -> {
//                            updateUiState {
//                                copy(
//                                    uiMessage = R.string.confirm_account,
//                                    signInStep = AuthSignInStep.CONFIRM_SIGN_UP
//                                )
//                            }
//                        }
//
//                        else -> {
//                            updateUiState {
//                                copy(
//                                    uiMessage = R.string.error_while_signing_in,
//                                    signInStep = null
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun getUserId() {
        viewModelScope.launch {
            when (val result = awsAuthCases.userGetId()) {
                is Resource.Success -> {
                    val userId = result.data
                    userId?.let { id ->
                        saveUserId(id)
//                        getUserInfo(userId)
                    }
                    updateUiState { copy(userId = result.data) }
                }

                is Resource.Error -> {
                    updateUiState { copy(userId = null) }
                }
            }
        }
    }

    private fun saveUserId(userId: String) {
        viewModelScope.launch {
            localUserDataStoreCases.saveUserId(userId)
        }
    }
// These lines are commented for the open source contribution.
//    private fun getUserInfo(userId: String) {
//        viewModelScope.launch {
//            when (val result = awsAuthCases.readUserInfo(userId)) {
//                is Resource.Success -> {
//                    result.data?.let { userInfo ->
//                        saveUserType(userInfo.userType)
//                        updateUiState { copy(signInStep = AuthSignInStep.DONE) }
//                    }
//                }
//
//                is Resource.Error -> {
//                    updateUiState { copy(uiMessage = R.string.error_while_signing_in) }
//                }
//            }
//        }
//    }

    private fun saveUserType(userType: String) {
        viewModelScope.launch {
            localUserDataStoreCases.saveUserType(userType)
        }
    }

//    fun signUp(email: String, password: String) {
//        viewModelScope.launch {
//            when (val result = awsAuthCases.userSignUp(email, password)) {
//                is AwsAuthSignUpResult.IsSignedUp -> {
//                    updateUiState {
//                        copy(
//                            uiMessage = R.string.signed_up,
//                            signUpStep = AuthSignUpStep.DONE
//                        )
//                    }
//                }
//
//                is AwsAuthSignUpResult.ConfirmSignUp -> {
//                    updateUiState {
//                        copy(
//                            uiMessage = R.string.verify_account,
//                            signUpStep = AuthSignUpStep.CONFIRM_SIGN_UP_STEP
//                        )
//                    }
//                }
//
//                is AwsAuthSignUpResult.Error -> {
//                    updateUiState {
//                        copy(
//                            signUpStep = null,
//                            uiMessage = result.message
//                        )
//                    }
//                }
//
//                is AwsAuthSignUpResult.ResentCode -> {
//                    //TODO: resend code
//                }
//            }
//        }
//    }

//    fun signInWithGoogle(activity: Activity) {
//        viewModelScope.launch {
//            when (val result = awsAuthCases.signInWithGoogle(activity)) {
//                is Resource.Success -> {
//                    val currUserId = result.data
//                    currUserId?.let { id ->
//                        updateUiState { copy(userId = id) }
//                        saveUserId(id)
//                        getUserInfo(id)
//                    }
//                }
//
//                is Resource.Error -> {
//                    updateUiState { copy(uiMessage = result.message) }
//                }
//            }
//        }
//    }

    fun clearUiMessage() {
        updateUiState { copy(uiMessage = null) }
    }

    fun clearAuthSignUpStep() {
        updateUiState { copy(signUpStep = null) }
    }

    fun clearAuthSignInStep() {
        updateUiState { copy(signInStep = null) }
    }
}