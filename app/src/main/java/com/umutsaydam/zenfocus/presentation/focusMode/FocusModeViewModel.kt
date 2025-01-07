package com.umutsaydam.zenfocus.presentation.focusMode

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.data.service.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FocusModeUiState(
    val remainingTime: String = "00:00",
    val remainingPercent: Float = 0f
)

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository,
    private val pomodoroManagerUseCase: PomodoroManagerUseCase,
    private val pomodoroServiceUseCases: PomodoroServiceUseCases,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FocusModeUiState())
    val uiState: StateFlow<FocusModeUiState> = _uiState

    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)

    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    init {
        getDefaultThemeName()
    }

    private fun updateUiState(update: FocusModeUiState.() -> FocusModeUiState) {
        _uiState.value = _uiState.value.update()
    }

    fun setTimer() {
        if (pomodoroServiceUseCases.isRunning()) {
            stopPomodoroService()
        }
        getRemainingTime()
        getRemainingPercent()
    }

    private fun isTimerRunning(): Boolean {
        return pomodoroManagerUseCase.isTimerRunning().value
    }

    private fun getRemainingTime() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingTimeAsTextFormat().collect { currRemainingTime ->
                updateUiState { copy(remainingTime = currRemainingTime) }
            }
        }
    }

    private fun getRemainingPercent() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingPercent().collect { currRemainingPercent ->
                updateUiState { copy(remainingPercent = currRemainingPercent) }
            }
        }
    }

    fun startPomodoroService() {
        if (isTimerRunning() && !PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.startService()
        }
    }

    private fun stopPomodoroService() {
        if (PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.stopService()
        }
    }

    private fun getDefaultThemeName() {
        viewModelScope.launch {
            val defaultThemeName = localUserDataStoreCases.readTheme()
            defaultThemeName.collect { themeName ->
                getTheme(themeName)
            }
        }
    }

    private fun getTheme(themeName: String) {
        viewModelScope.launch {
            val theme = themeRepository.getTheme(themeName)
            _defaultTheme.value = theme
        }
    }
}