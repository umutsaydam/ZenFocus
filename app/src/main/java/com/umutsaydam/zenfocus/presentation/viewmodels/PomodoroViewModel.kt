package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.data.foregroundService.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.TimeOutRingerManagerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.VibrationManagerUseCases
import com.umutsaydam.zenfocus.util.Constants.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PomodoroUiState(
    val remainingTime: String = "00:00",
    val remainingPercent: Float = 0f,
    val isTimerRunning: Boolean = false,
    val isWorkingSession: Boolean = true,
    val defaultSound: String = NONE,
)

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val timeOutRingerManagerUseCases: TimeOutRingerManagerUseCases,
    private val vibrationManagerUseCases: VibrationManagerUseCases,
    private val pomodoroManagerUseCase: PomodoroManagerUseCase,
    private val pomodoroServiceUseCases: PomodoroServiceUseCases,
    private val focusSoundUseCases: FocusSoundUseCases
) : ViewModel() {
    private val _pomodoroUiState = MutableStateFlow(PomodoroUiState())
    var pomodoroUiState: StateFlow<PomodoroUiState> = _pomodoroUiState

    init {
        isTimerRunning()
        setTimer()
        getDefaultFocusSound()
    }

    private fun updatePomodoroUiState(update: PomodoroUiState.() -> PomodoroUiState) {
        _pomodoroUiState.value = _pomodoroUiState.value.update()
    }

    fun setTimer() {
        if (PomodoroForegroundService.isServiceRunning) {
            stopPomodoroService()
        } else {
            // That means timer will be working for the first time.
            setDefaultPomodoroCycle()
            setDefaultPomodoroBreakDuration()
            setVibrateState()
            setTimeOutRingerState()
            setDefaultPomodoroWorkDuration()
        }
        getRemainingTime()
        getRemainingPercent()
        isWorkingSession()
    }

    private fun getDefaultFocusSound() {
        viewModelScope.launch {
            localUserDataStoreCases.readFocusSound().collectLatest { fileName ->
                updatePomodoroUiState { copy(defaultSound = fileName) }
                setSound(fileName)
            }
        }
    }

    private fun setSound(newSound: String) {
        focusSoundUseCases.setSound(newSound)
    }

    private fun isWorkingSession() {
        viewModelScope.launch {
            pomodoroManagerUseCase.isWorkingSession().collectLatest { isWorking ->
                updatePomodoroUiState { copy(isWorkingSession = isWorking) }
            }
        }
    }

    private fun setDefaultPomodoroCycle() {
        val pomodoroCycle = localUserDataStoreCases.readPomodoroCycle()

        viewModelScope.launch {
            pomodoroCycle.collectLatest { defaultCycle ->
                pomodoroManagerUseCase.setWorkCycle(defaultCycle)
            }
        }
    }

    private fun setDefaultPomodoroBreakDuration() {
        val breakDuration = localUserDataStoreCases.readPomodoroBreakDuration()

        viewModelScope.launch {
            breakDuration.collectLatest { duration ->
                pomodoroManagerUseCase.setBreakDurationAsMinute(duration)
            }
        }
    }

    private fun setDefaultPomodoroWorkDuration() {
        val workDuration = localUserDataStoreCases.readPomodoroWorkDuration()

        viewModelScope.launch {
            workDuration.collectLatest { duration ->
                pomodoroManagerUseCase.setWorkDurationAsMinute(duration)
            }
        }
    }

    fun playOrResumeTimer() {
        if (pomodoroUiState.value.isTimerRunning) {
            resumeTimer()
        } else {
            setTimer()
            playTimer()
        }
    }

    private fun playTimer() {
        pomodoroManagerUseCase.startPomodoro()
    }

    private fun resumeTimer() {
        pomodoroManagerUseCase.resumePomodoro()
    }

    private fun isTimerRunning() {
        viewModelScope.launch {
            pomodoroManagerUseCase.isTimerRunning().collect { isRunning ->
                updatePomodoroUiState { copy(isTimerRunning = isRunning) }
            }
        }
    }

    fun pauseTimer() {
        pomodoroManagerUseCase.pausePomodoro()
    }

    fun stopTimer() {
        pomodoroManagerUseCase.stopPomodoro()
    }

    private fun setVibrateState() {
        viewModelScope.launch {
            localUserDataStoreCases.readVibrateState().collectLatest { isEnabled ->
                vibrationManagerUseCases.setVibrateState(isEnabled)
            }
        }
    }

    private fun setTimeOutRingerState() {
        viewModelScope.launch {
            localUserDataStoreCases.readTimeOutRingerState().collectLatest { isEnabled ->
                timeOutRingerManagerUseCases.isRingerEnabled(isEnabled)
            }
        }
    }

    private fun getRemainingTime() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingTimeAsTextFormat().collect { currRemainingTime ->
                updatePomodoroUiState { copy(remainingTime = currRemainingTime) }
            }
        }
    }

    private fun getRemainingPercent() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingPercent().collect { currRemainingPercent ->
                updatePomodoroUiState { copy(remainingPercent = currRemainingPercent) }
            }
        }
    }

    fun startPomodoroService() {
        if (pomodoroUiState.value.isTimerRunning && !PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.startService()
        }
    }

    private fun stopPomodoroService() {
        if (PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.stopService()
        }
    }
}