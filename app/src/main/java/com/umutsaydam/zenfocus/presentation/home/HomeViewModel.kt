package com.umutsaydam.zenfocus.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.data.service.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.model.GoogleBannerAdState
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.model.UserTypeEnum
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import com.umutsaydam.zenfocus.util.Constants.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val toDoUsesCases: ToDoUsesCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val pomodoroManagerUseCase: PomodoroManagerUseCase,
    private val pomodoroServiceUseCases: PomodoroServiceUseCases,
    private val focusSoundUseCases: FocusSoundUseCases,
    private val checkerUseCases: NetworkCheckerUseCases,
    private val authCases: AwsAuthCases,
    private val googleProductsInAppUseCases: GoogleProductsInAppUseCases,
    private val googleAdUseCases: GoogleAdUseCases
) : ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)

    private val _userType = MutableStateFlow<String?>(null)
    private val userType: StateFlow<String?> = _userType

    private val _remainingTime = MutableStateFlow<String>("00:00")
    val remainingTime: StateFlow<String> = _remainingTime

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

    private val _focusSoundList = MutableStateFlow<Array<String>>(emptyArray())
    val focusSoundList: StateFlow<Array<String>> = _focusSoundList

    private val _defaultSound = MutableStateFlow<String>(NONE)
    val defaultSound = _defaultSound

    private val _uiMessage = MutableStateFlow<Int?>(null)
    val uiMessage: StateFlow<Int?> = _uiMessage

    private val _adState = MutableStateFlow(GoogleBannerAdState())
    val adState: StateFlow<GoogleBannerAdState> = _adState

    init {
        getTasks()
        getUserId()
        getUserType()
        isTimerRunning()
        setTimer()
        getSoundList()
        getDefaultFocusSound()
    }

    private fun getSoundList() {
        _focusSoundList.value = focusSoundUseCases.readSoundList()
    }

    private fun getDefaultFocusSound() {
        viewModelScope.launch {
            localUserDataStoreCases.readFocusSound().collectLatest { fileName ->
                _defaultSound.value = fileName
                setSound(fileName)
            }
        }
    }

    fun setDefaultSoundAndPlay(newSound: String) {
        if (_defaultSound.value != newSound) {
            _defaultSound.value = newSound
            saveDefaultSound(newSound)
        }
    }

    private fun saveDefaultSound(newSound: String) {
        viewModelScope.launch {
            localUserDataStoreCases.saveFocusSound(newSound)
            setSound(newSound)
            playSound()
        }
    }

    private fun setSound(newSound: String) {
        focusSoundUseCases.setSound(newSound)
    }

    private fun playSound() {
        focusSoundUseCases.playSoundIfAvailable()
    }

    fun stopSound() {
        focusSoundUseCases.stopSound()
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
            toDoUsesCases.getTasks().collect { list ->
                _toDoList.value = list
            }
        }
    }

    fun upsertTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.upsertTask(taskModel)
        }
    }

    fun deleteTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.deleteTask.invoke(taskModel)
            getTasks()
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

    fun isNetworkConnected(): Boolean {
        return checkerUseCases.isConnected()
    }

    fun willShowAd(): Boolean {
        return _userType.value != UserTypeEnum.AD_FREE_USER.type
    }

    private fun getUserType() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserType().collectLatest { type ->
                _userType.value = type
                Log.i("R/T", "_userType.value in viewmodel ${_userType.value}")
            }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            _userId.value = localUserDataStoreCases.readUserId().first()
        }
    }

    private fun changeUserTypeAsAdFree() {
        viewModelScope.launch {
            val adFreeUser = UserTypeEnum.AD_FREE_USER.type
            if (_userId.value != null && _userType.value != adFreeUser) {
                val result = authCases.updateUserInfo(
                    userId = _userId.value!!,
                    userType = adFreeUser
                )

                when (result) {
                    is Resource.Success -> {
                        _uiMessage.value = R.string.became_ad_free_user
                    }

                    is Resource.Error -> {
                        _uiMessage.value = R.string.error_occurred_ad_free
                    }
                }
            }
        }
    }

    fun showBannerAd(adSize: AdSize): AdView {
       return googleAdUseCases.showBannerAd(
            adSize = adSize,
            onAdLoaded = { isLoaded ->
                _adState.value = _adState.value.copy(isAdLoaded = isLoaded)
            },
            onFirstAdRequested = { isFirstAdRequested ->
                _adState.value = _adState.value.copy(isFirstAdRequested = isFirstAdRequested)
            }
        )
    }

    fun startProductsInApp() {
        googleProductsInAppUseCases.startConnection()

        viewModelScope.launch {
            googleProductsInAppUseCases.observePurchaseStateFlow().collect { purchaseState ->
                if (purchaseState) {
                    changeUserTypeAsAdFree()
                }
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