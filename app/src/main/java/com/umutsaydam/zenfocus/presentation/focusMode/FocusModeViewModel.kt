package com.umutsaydam.zenfocus.presentation.focusMode

import android.graphics.Bitmap
import android.util.Log
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

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository,
    private val pomodoroManagerUseCase: PomodoroManagerUseCase,
    private val pomodoroServiceUseCases: PomodoroServiceUseCases,
) : ViewModel() {
    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)
    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    private val _remainingTime = MutableStateFlow<String>("00:00")
    val remainingTime: StateFlow<String> = _remainingTime

    private val _remainingPercent = MutableStateFlow<Float>(0f)
    val remainingPercent: StateFlow<Float> = _remainingPercent

    init {
        getDefaultThemeName()
    }

    fun setTimer() {
        Log.i(
            "R/T",
            "PomodoroForegroundService.isServiceRunning: ${pomodoroServiceUseCases.isRunning()}"
        )
        if (pomodoroServiceUseCases.isRunning()) {
            Log.i("R/T", "Service was stopped. Switching normal timer...")
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
                _remainingTime.value = currRemainingTime
            }
        }
    }

    private fun getRemainingPercent() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingPercent().collect { currRemainingPercent ->
                _remainingPercent.value = currRemainingPercent
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
                Log.i("R/T", "theme: $themeName")
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