package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val defaultVibrateState: Boolean = false,
    val defaultTimeOutRingerState: Boolean = false,
    val isSignedInState: Boolean = false,
    val pomodoroWorkDuration: Int = 0,
    val tmpPomodoroWorkDuration: Int = 0,
    val pomodoroBreakDuration: Int = 0,
    val tmpPomodoroBreakDuration: Int = 0
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val awsAuthCases: AwsAuthCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        readVibrateState()
        readTimeOutRingerState()
        isSignedIn()
        getPomodoroWorkDuration()
        getPomodoroBreakDuration()
    }

    private fun updateUiState(update: SettingsUiState.() -> SettingsUiState) {
        _uiState.value = _uiState.value.update()
    }

    private fun readVibrateState() {
        viewModelScope.launch {
            localUserDataStoreCases.readVibrateState().collect { state ->
                updateUiState { copy(defaultVibrateState = state) }
            }
        }
    }

    fun setVibrateState(state: Boolean) {
        updateUiState { copy(defaultVibrateState = state) }

        viewModelScope.launch {
            localUserDataStoreCases.saveVibrateState(state)
        }
    }

    private fun readTimeOutRingerState() {
        viewModelScope.launch {
            localUserDataStoreCases.readTimeOutRingerState().collectLatest { state ->
                updateUiState { copy(defaultTimeOutRingerState = state) }
            }
        }
    }

    fun setTimeOutRingerState(state: Boolean) {
        viewModelScope.launch {
            localUserDataStoreCases.saveTimeOutRingerState(state)
        }
    }

    private fun isSignedIn() {
        viewModelScope.launch {
            val userId = localUserDataStoreCases.readUserId()
            userId.collect { value ->
                updateUiState { copy(isSignedInState = value.isNotEmpty()) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            updateUiState { copy(isSignedInState = false) }
            awsAuthCases.signOut()
            localUserDataStoreCases.deleteUserId()
            localUserDataStoreCases.deleteUserType()
        }
    }

    private fun getPomodoroWorkDuration() {
        viewModelScope.launch {
            localUserDataStoreCases.readPomodoroWorkDuration().collectLatest { workDuration ->
                updateUiState {
                    copy(
                        pomodoroWorkDuration = workDuration,
                        tmpPomodoroWorkDuration = workDuration
                    )
                }
            }
        }
    }

    fun savePomodoroWorkDuration() {
        viewModelScope.launch {
            localUserDataStoreCases.savePomodoroWorkDuration(uiState.value.tmpPomodoroWorkDuration)
        }
    }

    private fun getPomodoroBreakDuration() {
        viewModelScope.launch {
            localUserDataStoreCases.readPomodoroBreakDuration().collectLatest { breakDuration ->
                updateUiState {
                    copy(
                        pomodoroBreakDuration = breakDuration,
                        tmpPomodoroBreakDuration = breakDuration
                    )
                }
            }
        }
    }

    fun savePomodoroBreakDuration() {
        viewModelScope.launch {
            localUserDataStoreCases.savePomodoroBreakDuration(uiState.value.tmpPomodoroBreakDuration)
        }
    }

    fun updateWorkDuration(currIndex: Int) {
        updateUiState { copy(tmpPomodoroWorkDuration = currIndex) }
    }

    fun updateBreakDuration(currIndex: Int) {
        updateUiState { copy(tmpPomodoroBreakDuration = currIndex) }
    }
}