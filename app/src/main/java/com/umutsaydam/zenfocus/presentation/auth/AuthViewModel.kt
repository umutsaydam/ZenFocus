package com.umutsaydam.zenfocus.presentation.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val awsAuthCases: AwsAuthCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val networkCheckerUseCases: NetworkCheckerUseCases
) : ViewModel() {
    private val _signUpStep: MutableStateFlow<AuthSignUpStep?> = MutableStateFlow(null)
    var signUpStep: StateFlow<AuthSignUpStep?> = _signUpStep

    private val _signInStep: MutableStateFlow<AuthSignInStep?> = MutableStateFlow(null)
    val signInStep: StateFlow<AuthSignInStep?> = _signInStep

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun isConnected(): Boolean {
        return networkCheckerUseCases.isConnected()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result: AwsAuthSignInResult = awsAuthCases.userSignIn(email, password)
            Log.i("R/T", "$result")

            when (result) {
                is AwsAuthSignInResult.IsSignedIn -> {
                    getUserId()
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
                        getUserInfo(userId)
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
            localUserDataStoreCases.saveUserId(userId)
            Log.i("R/T", "id saved $userId")
        }
    }

    private fun getUserInfo(userId: String) {
        viewModelScope.launch {
            val result = awsAuthCases.readUserInfo(userId)
            Log.i("R/T", "/////// in viewmodel : $result")
            when (result) {
                is Resource.Success -> {
                    Log.i("R/T", "*/*/**/*//*/*/*/*/* in viewmodel : ${result.data}")
                    result.data?.let { userInfo ->
                        Log.i("R/T", "*/*/*/*/*/*/*  $userInfo")
                        saveUserType(userInfo.userType)
                        _signInStep.value = AuthSignInStep.DONE
                    }
                }

                is Resource.Error -> {
                    Log.i("R/T", "in viewmodel err: ${result.message}")
                }

                is Resource.Loading -> {
                    Log.i("R/T", "in viewmodel loading: loading")
                }
            }
        }
    }

    private fun saveUserType(userType: String) {
        viewModelScope.launch {
            localUserDataStoreCases.saveUserType(userType)
            Log.i("R/T", "Saved userType. $userType")
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

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            val result = awsAuthCases.signInWithGoogle(activity)
            Log.i("R/T", "resu in viewmodel:  $result")

            when (result) {
                is Resource.Success -> {
                    val currUserId = result.data
                    currUserId?.let { id ->
                        _userId.value = id
                        saveUserId(id)
                        getUserInfo(id)
                    }
                }

                is Resource.Error -> {
                    Log.e("R/T", "error in viewmodel: ${result.message}")
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}