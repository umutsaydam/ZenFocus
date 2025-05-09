package com.umutsaydam.zenfocus.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.model.UserTypeEnum
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import com.umutsaydam.zenfocus.presentation.home.components.BottomSheetContent
import com.umutsaydam.zenfocus.util.Constants.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val userId: String = "",
    val userType: String? = null,
    val toDoList: List<TaskModel> = emptyList(),
    val sliderPosition: Float = 1f,
    val bottomSheetContent: BottomSheetContent? = null,
    val bottomSheetState: Boolean = false,
    val defaultSound: String = NONE,
    val uiMessage: Int? = null,
)

data class GoogleBannerAdState(
    val isAdLoaded: Boolean = false, val isFirstAdRequested: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val toDoUsesCases: ToDoUsesCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val focusSoundUseCases: FocusSoundUseCases,
    private val checkerUseCases: NetworkCheckerUseCases,
    private val authCases: AwsAuthCases,
    private val googleProductsInAppUseCases: GoogleProductsInAppUseCases,
    private val googleAdUseCases: GoogleAdUseCases
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    private val _focusSoundList = MutableStateFlow<Array<String>>(emptyArray())
    val focusSoundList: StateFlow<Array<String>> = _focusSoundList

    private val _adState = MutableStateFlow(GoogleBannerAdState())
    val adState: StateFlow<GoogleBannerAdState> = _adState

    init {
        getTasks()
        getUserId()
        getUserType()
        getDefaultFocusSound()
        getSliderPosition()
    }

    private fun updateHomeUiState(update: HomeUiState.() -> HomeUiState) {
        _homeUiState.value = _homeUiState.value.update()
    }

    fun getSoundList() {
        _focusSoundList.value = focusSoundUseCases.readSoundList()
    }

    private fun getDefaultFocusSound() {
        viewModelScope.launch {
            localUserDataStoreCases.readFocusSound().collectLatest { fileName ->
                updateHomeUiState { copy(defaultSound = fileName) }
                setSound(fileName)
            }
        }
    }

    fun setDefaultSoundAndPlay(newSound: String) {
        if (homeUiState.value.defaultSound != newSound) {
            updateHomeUiState { copy(defaultSound = newSound) }
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

    private fun getTasks() {
        viewModelScope.launch {
            toDoUsesCases.getTasks().collect { list ->
                updateHomeUiState { copy(toDoList = list) }
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
            toDoUsesCases.deleteTask(taskModel)
            getTasks()
        }
    }

    private fun getSliderPosition() {
        viewModelScope.launch {
            val cycle = localUserDataStoreCases.readPomodoroCycle().first()

            updateHomeUiState { copy(sliderPosition = cycle.toFloat()) }
        }
    }

    fun setSliderPosition(position: Float) {
        if (_homeUiState.value.sliderPosition != position) {
            updateHomeUiState { copy(sliderPosition = position) }
            val newCycle = position.toInt()

            viewModelScope.launch {
                localUserDataStoreCases.savePomodoroCycle(newCycle)
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        return checkerUseCases.isConnected()
    }

    fun shouldShowAd(): Boolean {
        return homeUiState.value.userType != UserTypeEnum.AD_FREE_USER.type
    }

    private fun getUserType() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserType().collectLatest { type ->
                updateHomeUiState { copy(userType = type) }
            }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserId().collectLatest { id ->
                updateHomeUiState { copy(userId = id) }
            }
        }
    }

    private fun changeUserTypeAsAdFree() {
        viewModelScope.launch {
            val adFreeUser = UserTypeEnum.AD_FREE_USER.type
            if (homeUiState.value.userId.isNotEmpty() && homeUiState.value.userType != adFreeUser) {
                val result = authCases.updateUserInfo(
                    userId = homeUiState.value.userId, userType = adFreeUser
                )

                when (result) {
                    is Resource.Success -> {
                        updateHomeUiState { copy(uiMessage = R.string.became_ad_free_user) }
                    }

                    is Resource.Error -> {
                        updateHomeUiState { copy(uiMessage = R.string.error_occurred_ad_free) }
                    }
                }
            }
        }
    }

    fun showBannerAd(adSize: AdSize): AdView {
        return googleAdUseCases.showBannerAd(adSize = adSize, onAdLoaded = { isLoaded ->
            _adState.value = _adState.value.copy(isAdLoaded = isLoaded)
        }, onFirstAdRequested = { isFirstAdRequested ->
            _adState.value = _adState.value.copy(isFirstAdRequested = isFirstAdRequested)
        })
    }

    fun startProductsInApp(activity: Activity) {
        if (_homeUiState.value.userId.isNotEmpty()) {
            googleProductsInAppUseCases.startConnection(activity)

            viewModelScope.launch {
                googleProductsInAppUseCases.observePurchaseStateFlow().collect { purchaseState ->
                    if (purchaseState) {
                        changeUserTypeAsAdFree()
                    }
                }
            }
        } else {
            updateHomeUiState { copy(uiMessage = R.string.must_sign_in_remove_ad) }
        }
    }

    private fun setBottomSheetContent(content: BottomSheetContent) {
        updateHomeUiState { copy(bottomSheetContent = content) }
    }

    fun setBottomSheetState(state: Boolean) {
        updateHomeUiState { copy(bottomSheetState = state) }
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

    fun clearUiMessage() {
        updateHomeUiState { copy(uiMessage = null) }
    }
}