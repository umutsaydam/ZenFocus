package com.umutsaydam.zenfocus.presentation.settings

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

data class SettingsUiState(
    val defaultVibrateState: Boolean = false,
    val isSignedInState: Boolean = false,
    val pomodoroWorkDuration: Int = 0,
    val pomodoroBreakDuration: Int = 0
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
            val workDuration = localUserDataStoreCases.readPomodoroWorkDuration().first()
            updateUiState { copy(pomodoroWorkDuration = workDuration) }
        }
    }

    fun savePomodoroWorkDuration(newDuration: Int) {
        viewModelScope.launch {
            localUserDataStoreCases.savePomodoroWorkDuration(newDuration)
        }
    }

    private fun getPomodoroBreakDuration() {
        viewModelScope.launch {
            val breakDuration = localUserDataStoreCases.readPomodoroBreakDuration().first()
            updateUiState { copy(pomodoroBreakDuration = breakDuration) }
        }
    }

    fun savePomodoroBreakDuration(newDuration: Int) {
        viewModelScope.launch {
            localUserDataStoreCases.savePomodoroBreakDuration(newDuration)
        }
    }

    fun updateWorkDuration(currIndex: Int) {
        updateUiState { copy(pomodoroWorkDuration = currIndex) }
    }

    fun updateBreakDuration(currIndex: Int) {
        updateUiState { copy(pomodoroBreakDuration = currIndex) }
    }
}