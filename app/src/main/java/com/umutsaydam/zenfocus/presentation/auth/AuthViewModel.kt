package com.umutsaydam.zenfocus.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.util.AwsAuthSignInResult
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val awsAuthCases: AwsAuthCases,
    private val localUserCases: LocalUserCases
) : ViewModel() {
    private val _signUpStep: MutableStateFlow<AuthSignUpStep?> = MutableStateFlow(null)
    var signUpStep: StateFlow<AuthSignUpStep?> = _signUpStep

    private val _signInStep: MutableStateFlow<AuthSignInStep?> = MutableStateFlow(null)
    val signInStep: StateFlow<AuthSignInStep?> = _signInStep

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)
    var userId: StateFlow<String?> = _userId

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result: AwsAuthSignInResult = awsAuthCases.userSignIn(email, password)
            Log.i("R/T", "$result")

            when (result) {
                is AwsAuthSignInResult.IsSignedIn -> {
                    getUserId()
                    _signInStep.value = AuthSignInStep.DONE
                }

                is AwsAuthSignInResult.ConfirmSignIn -> {
                    _userId.value = null
                    _signInStep.value = AuthSignInStep.CONFIRM_SIGN_UP
                    Log.i("R/T", "confirm $result")
                }

                is AwsAuthSignInResult.Error -> {
                    Log.i("R/T", "error : $result")
                    _userId.value = null
                    _signInStep.value = null
                    _errorMessage.value = result.exception.message
                }
            }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            val result = awsAuthCases.userGetId.invoke()
            Log.i("R/T", "$result")

            when (result) {
                is Resource.Success -> {
                    Log.i("R/T", "in viewmodel ${result.data}")
                    val userId = result.data
                    userId?.let { id ->
                        saveUserId(id)
                    }
                    _userId.value = result.data
                }

                is Resource.Error -> {
                    _userId.value = null
                    _errorMessage.value = result.message
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun saveUserId(userId: String) {
        viewModelScope.launch {
            localUserCases.saveUserId(userId)
            Log.i("R/T", "id saved $userId")

            getUserInfo(userId)
        }
    }

    private fun getUserInfo(userId: String) {
        viewModelScope.launch {
            val result = awsAuthCases.readUserInfo(userId)
            Log.i("R/T", "in viewmodel : $result")
            when (result) {
                is Resource.Success -> {
                    Log.i("R/T", "in viewmodel : ${result.data}")
                    result.data?.let { userInfo ->
                        saveUserType(userInfo.userType)
                    }
                }

                is Resource.Error -> {
                    Log.i("R/T", "in viewmodel : ${result.message}")
                }

                is Resource.Loading -> {
                    Log.i("R/T", "in viewmodel : loading")
                }
            }
        }
    }

    private fun saveUserType(userType: String) {
        viewModelScope.launch {
            localUserCases.saveUserType(userType)
            Log.i("R/T", "Saved userType.")
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = awsAuthCases.userSignUp(email, password)
            Log.i("R/T", "in viewmodel => $result")

            when (result) {
                is AwsAuthSignUpResult.IsSignedUp -> {
                    _signUpStep.value = AuthSignUpStep.DONE
                }

                is AwsAuthSignUpResult.ConfirmSignUp -> {
                    _signUpStep.value = AuthSignUpStep.CONFIRM_SIGN_UP_STEP
                }

                is AwsAuthSignUpResult.Error -> {
                    _signUpStep.value = null
                    _errorMessage.value = result.exception.message
                }
            }
        }
    }

    fun signInWithGoogle() {
        //TODO perform sign in with google
    }
}