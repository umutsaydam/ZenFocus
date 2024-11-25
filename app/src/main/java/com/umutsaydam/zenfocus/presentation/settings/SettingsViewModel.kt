package com.umutsaydam.zenfocus.presentation.settings

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localUserCases: LocalUserCases
) : ViewModel() {
    private val _defaultVibrateState = MutableStateFlow(false)
    val defaultVibrateState: StateFlow<Boolean> get() = _defaultVibrateState

    init {
        viewModelScope.launch {
            localUserCases.readVibrateState.invoke().collect { state ->
                _defaultVibrateState.value = state
            }
        }
    }

    fun setVibrateState(state: Boolean) {
        _defaultVibrateState.value = state

        viewModelScope.launch {
            localUserCases.saveVibrateState.invoke(state)
        }
    }
}