package com.umutsaydam.zenfocus.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    private val _pomodoroWorkDuration = MutableStateFlow(0)
    val pomodoroWorkDuration: StateFlow<Int> = _pomodoroWorkDuration

    private val _pomodoroBreakDuration = MutableStateFlow(0)
    val pomodoroBreakDuration: StateFlow<Int> = _pomodoroBreakDuration

    init {
        readVibrateState()
        isSignedIn()
        getPomodoroWorkDuration()
        getPomodoroBreakDuration()
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
                Log.i("R/T", "value $value")
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

    private fun getPomodoroWorkDuration() {
        viewModelScope.launch {
            _pomodoroWorkDuration.value = localUserDataStoreCases.readPomodoroWorkDuration().first()
        }
    }

    fun savePomodoroWorkDuration(newDuration: Int) {
        if (_pomodoroWorkDuration.value != newDuration) {
            viewModelScope.launch {
                Log.i("R/T", "Working newDuration: $newDuration")
                _pomodoroWorkDuration.value = newDuration
                localUserDataStoreCases.savePomodoroWorkDuration(newDuration)
            }
        }
    }

    private fun getPomodoroBreakDuration() {
        viewModelScope.launch {
            _pomodoroBreakDuration.value =
                localUserDataStoreCases.readPomodoroBreakDuration().first()
        }
    }

    fun savePomodoroBreakDuration(newDuration: Int) {
        if (_pomodoroBreakDuration.value != newDuration) {
            viewModelScope.launch {
                Log.i("R/T", "Breaking newDuration: $newDuration")
                _pomodoroBreakDuration.value = newDuration
                localUserDataStoreCases.savePomodoroBreakDuration(newDuration)
            }
        }
    }
}