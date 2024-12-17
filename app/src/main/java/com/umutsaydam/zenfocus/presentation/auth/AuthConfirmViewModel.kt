package com.umutsaydam.zenfocus.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthConfirmViewModel @Inject constructor(
    private val awsAuthCases: AwsAuthCases,
    private val networkCheckerUseCases: NetworkCheckerUseCases
) : ViewModel() {
    private val _userConfirmState: MutableStateFlow<AuthSignUpStep?> = MutableStateFlow(null)
    var userConfirmState: StateFlow<AuthSignUpStep?> = _userConfirmState

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun isConnected(): Boolean {
        return networkCheckerUseCases.isConnected()
    }

    fun accountConfirm(email: String, confirmCode: String) {
        viewModelScope.launch {
            val result = awsAuthCases.userSignUpConfirm(email, confirmCode)
            Log.i("R/T", "in viewmodel: $result")
            when (result) {
                is AwsAuthSignUpResult.IsSignedUp -> {
                    _userConfirmState.value = AuthSignUpStep.DONE
                }

                is AwsAuthSignUpResult.Error -> {
                    _userConfirmState.value = null
                    _errorMessage.value = result.exception.message
                }

                else -> {
                    _userConfirmState.value = null
                }
            }
        }
    }
}