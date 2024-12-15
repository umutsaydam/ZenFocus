package com.umutsaydam.zenfocus.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val awsAuthCases: AwsAuthCases
) : ViewModel() {
    private val _defaultVibrateState = MutableStateFlow(false)
    val defaultVibrateState: StateFlow<Boolean> get() = _defaultVibrateState

    private val _isSignedInState = MutableStateFlow(false)
    val isSignedInState: StateFlow<Boolean> = _isSignedInState

    init {
        readVibrateState()
        isSignedIn()
    }

    private fun readVibrateState() {
        viewModelScope.launch {
            localUserDataStoreCases.readVibrateState.invoke().collect { state ->
                _defaultVibrateState.value = state
            }
        }
    }

    fun setVibrateState(state: Boolean) {
        _defaultVibrateState.value = state

        viewModelScope.launch {
            localUserDataStoreCases.saveVibrateState.invoke(state)
        }
    }

    private fun isSignedIn() {
        viewModelScope.launch {
            val userId = localUserDataStoreCases.readUserId.invoke()
            userId.collect { value ->
                _isSignedInState.value = value.isNotEmpty()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _isSignedInState.value = false
            awsAuthCases.signOut.invoke()
            localUserDataStoreCases.deleteUserId.invoke()
            localUserDataStoreCases.deleteUserType.invoke()
        }
    }
}