package com.umutsaydam.zenfocus.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.data.service.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val toDoUsesCases: ToDoUsesCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val pomodoroManagerUseCase: PomodoroManagerUseCase,
    private val pomodoroServiceUseCases: PomodoroServiceUseCases,
) : ViewModel() {
    private val _remainingTime = MutableStateFlow<String>("00:00")
    val remainingTime: StateFlow<String> = _remainingTime

    private val _remainingTimeMilli = MutableStateFlow<Long>(0L)
    val remainingTimeMilli: StateFlow<Long> = _remainingTimeMilli

    private val _remainingPercent = MutableStateFlow<Float>(0f)
    val remainingPercent: StateFlow<Float> = _remainingPercent

    private val _isTimerRunning = MutableStateFlow<Boolean>(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _toDoList = MutableStateFlow<List<TaskModel>>(emptyList())
    val toDoList: StateFlow<List<TaskModel>> = _toDoList

    private val _sliderPosition = MutableStateFlow<Float>(1f)
    val sliderPosition = _sliderPosition

    private val _bottomSheetContent = MutableStateFlow<BottomSheetContent?>(null)
    val bottomSheetContent: StateFlow<BottomSheetContent?> = _bottomSheetContent

    private val _bottomSheetState = MutableStateFlow<Boolean>(false)
    val bottomSheetState: StateFlow<Boolean> = _bottomSheetState

    private val _soundList = MutableStateFlow<List<String>>(
        listOf(
            "None",
            "LoFi Rainy",
            "Lorem Ipsum",
            "Dolor Lorem",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit1"
        )
    )

    val soundList: StateFlow<List<String>> = _soundList

    private val _defaultSound = MutableStateFlow<String>("defaultSound")
    val defaultSound = _defaultSound

    init {
        getTasks()
        isTimerRunning()
        setTimer()
    }

    fun setTimer() {
        Log.i(
            "R/T",
            "PomodoroForegroundService.isServiceRunning: ${PomodoroForegroundService.isServiceRunning}"
        )
        if (PomodoroForegroundService.isServiceRunning) {
            Log.i("R/T", "Service was stopped. Switching normal timer...")
            stopPomodoroService()
        } else {
            // That means timer will be working for the first time.
            Log.i("R/T", "Service was not working so switching default pomodoro timer.")
            setDefaultPomodoroCycle()
            setDefaultPomodoroBreakDuration()
            setVibrateState()
            setDefaultPomodoroWorkDuration()
        }
        getRemainingTime()
        getRemainingTimeMilli()
        getRemainingPercent()
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

    private fun getRemainingTimeMilli() {
        viewModelScope.launch {
            pomodoroManagerUseCase.getRemainingTimeMilli().collect { timeMilli ->
                _remainingTimeMilli.value = timeMilli
            }
        }
    }

    fun playOrResumeTimer() {
        if (_isTimerRunning.value) {
            Log.i("R/T", "playTimer fun was RESUMED. in viewmodel")
            resumeTimer()
        } else {
            Log.i("R/T", "playTimer fun was STARTED first time. in viewmodel")
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
                _isTimerRunning.value = isRunning
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
        val isVibrateEnabled = localUserDataStoreCases.readVibrateState()

        viewModelScope.launch {
            isVibrateEnabled.collectLatest { isEnabled ->
                pomodoroManagerUseCase.setVibrateState(isEnabled)
            }
        }
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
        if (_isTimerRunning.value && !PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.startService()
        }
    }

    private fun stopPomodoroService() {
        if (PomodoroForegroundService.isServiceRunning) {
            pomodoroServiceUseCases.stopService()
        }
    }

    private fun getTasks() {
        viewModelScope.launch {
            _toDoList.value = toDoUsesCases.getTasks.invoke()
        }
    }

    fun upsertTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.upsertTask.invoke(taskModel)
            getTasks()
        }
    }

    fun deleteTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.deleteTask.invoke(taskModel)
            getTasks()
        }
    }

    fun setDefaultSound(newSound: String) {
        if (_defaultSound.value != newSound) {
            _defaultSound.value = newSound
        }
    }

    fun setSliderPosition(position: Float) {
        if (_sliderPosition.value != position) {
            _sliderPosition.value = position
            val newCycle = position.toInt()

            viewModelScope.launch {
                localUserDataStoreCases.savePomodoroCycle(newCycle)
                Log.i("R/T", "newCycle -> $newCycle")
            }
        }
    }

    private fun setBottomSheetContent(content: BottomSheetContent) {
        _bottomSheetContent.value = content
    }

    fun setBottomSheetState(state: Boolean) {
        _bottomSheetState.value = state
    }

    fun showPomodoroTimesBottomSheet() {
        setBottomSheetContent(BottomSheetContent.PomodoroTimes)
        setBottomSheetState(true)
    }

    fun showPomodoroSoundsBottomSheet() {
        setBottomSheetContent(BottomSheetContent.PomodoroSounds)
        setBottomSheetState(true)
    }

    fun showAddToDoBottomSheet() {
        setBottomSheetContent(BottomSheetContent.AddToDo)
        setBottomSheetState(true)
    }
}