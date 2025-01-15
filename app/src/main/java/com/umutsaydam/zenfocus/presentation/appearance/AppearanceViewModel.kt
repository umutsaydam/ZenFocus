package com.umutsaydam.zenfocus.presentation.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.rewarded.RewardedAd
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsStorageCases
import com.umutsaydam.zenfocus.util.FileNameFromUrl
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.model.UserTypeEnum
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppearanceUiState(
    val themeList: List<ThemeInfo?> = listOf(null),
    val selectedTheme: ThemeInfo? = null,
    val rewardedAd: RewardedAd? = null,
    val uiMessage: Int? = null
)

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val awsStorageCases: AwsStorageCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository,
    private val networkCheckerUseCases: NetworkCheckerUseCases,
    private val googleAddUseCases: GoogleAdUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppearanceUiState())
    val uiState: StateFlow<AppearanceUiState> = _uiState

    private val _userType = MutableStateFlow<String?>(null)

    init {
        getThemeList()
        getUserType()
    }

    private fun updateUiState(update: AppearanceUiState.() -> AppearanceUiState) {
        _uiState.value = _uiState.value.update()
    }

    fun isConnected(): Boolean {
        return networkCheckerUseCases.isConnected()
    }

    fun setDefaultTheme(newTheme: ThemeInfo?) {
        if (newTheme != null && _uiState.value.selectedTheme != newTheme) {
            updateUiState { copy(selectedTheme = newTheme) }
        }
    }

    fun saveTheme() {
        uiState.value.selectedTheme?.let {
            val themeUrl = it.themeUrl
            val newThemeName = FileNameFromUrl.getFileNameFromUrl(themeUrl)

            if (themeRepository.isThemeAvailableInLocalStorage(newThemeName)) {
                saveThemeToLocal(newThemeName)
            } else {
                downloadSelectedTheme(selectedThemeUrl = themeUrl)
            }
        }
    }

    private fun saveThemeToLocal(themeName: String) {
        viewModelScope.launch {
            localUserDataStoreCases.saveTheme(themeName)
        }
    }

    private fun getThemeList() {
        if (isConnected()) {
            viewModelScope.launch {
                updateUiState { copy(uiMessage = R.string.loading) }

                when (val themesResult: Resource<APIResponse> =
                    awsStorageCases.readThemeList()) {
                    is Resource.Success -> {
                        themesResult.data?.let {
                            val updatedThemeList = listOf(null) + it.body.items + listOf(null)
                            updateUiState { copy(themeList = updatedThemeList) }
                        }
                    }

                    is Resource.Error -> {
                        updateUiState {
                            copy(
                                themeList = listOf(null),
                                uiMessage = R.string.error_occurred_themes_list
                            )
                        }
                    }
                }
            }
        }
    }

    private fun downloadSelectedTheme(selectedThemeUrl: String) {
        viewModelScope.launch {
            val themeResource: Resource<String> =
                awsStorageCases.downloadSelectedThemeList(selectedThemeUrl)
            updateUiState { copy(uiMessage = R.string.loading) }

            when (themeResource) {
                is Resource.Success -> {
                    themeResource.data?.let { themeName ->
                        localUserDataStoreCases.saveTheme(themeName)
                        updateUiState { copy(uiMessage = R.string.new_theme_set) }
                    }
                }

                is Resource.Error -> {
                    updateUiState {
                        copy(
                            selectedTheme = null,
                            uiMessage = R.string.new_theme_setting_error
                        )
                    }
                }
            }
        }
    }

    private fun getUserType() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserType().collectLatest { type ->
                _userType.value = type
            }
        }
    }

    fun willShowAd(): Boolean {
        return _userType.value != UserTypeEnum.AD_FREE_USER.type
    }

    fun showRewardedAd() {
        viewModelScope.launch {
            val rewardResult = googleAddUseCases.showRewardedAd()
            updateUiState { copy(rewardedAd = rewardResult) }
        }
    }

    fun clearUiMessage() {
        updateUiState { copy(uiMessage = null) }
    }
}