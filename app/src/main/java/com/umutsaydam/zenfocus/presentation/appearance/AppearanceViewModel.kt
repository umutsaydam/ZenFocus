package com.umutsaydam.zenfocus.presentation.appearance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val awsStorageCases: AwsStorageCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository,
    private val networkCheckerUseCases: NetworkCheckerUseCases
) : ViewModel() {
    private val _userType = MutableStateFlow<String?>(null)
    private val userType: StateFlow<String?> = _userType

    private val _themeList = MutableStateFlow<List<ThemeInfo?>>(listOf(null))
    val themeList: StateFlow<List<ThemeInfo?>> = _themeList

    private val _uiMessage = MutableStateFlow<Int?>(null)
    val uiMessage: StateFlow<Int?> = _uiMessage

    private val _defaultTheme = MutableStateFlow<ThemeInfo?>(null)
    val defaultTheme: StateFlow<ThemeInfo?> = _defaultTheme

    init {
        getThemeList()
        getUserType()
    }

    fun isConnected(): Boolean {
        return networkCheckerUseCases.isConnected()
    }

    fun setDefaultTheme(newTheme: ThemeInfo?) {
        if (newTheme != null && defaultTheme.value != newTheme) {
            _defaultTheme.value = newTheme

            val newThemeName = FileNameFromUrl.getFileNameFromUrl(newTheme.themeUrl)
            if (themeRepository.isThemeAvailableInLocalStorage(newThemeName)) {
                Log.i("R/T", "Selected theme is already downloaded.")
                viewModelScope.launch {
                    localUserDataStoreCases.saveTheme(newThemeName)
                }
            } else {
                downloadSelectedTheme(selectedThemeUrl = newTheme.themeUrl)
            }
        }
    }

    private fun getThemeList() {
        Log.i("R/T", "network state: ${networkCheckerUseCases.isConnected()}")
        if (isConnected()) {
            viewModelScope.launch {
                _uiMessage.value = R.string.loading

                when (val themesResult: Resource<APIResponse> =
                    awsStorageCases.readThemeList.invoke()) {
                    is Resource.Success -> {
                        Log.i("R/T", "_themeList = $themesResult in viewmodel")
                        themesResult.data?.let {
                            val updatedThemeList = listOf(null) + it.body.items + listOf(null)
                            _themeList.value = updatedThemeList
                        }
                    }

                    is Resource.Error -> {
                        _themeList.value = listOf(null)
                        _uiMessage.value = R.string.error_occurred_themes_list
                        Log.i("R/T", "Error in viewmodel: ${themesResult.message}")
                    }
                }
            }
        }
    }

    private fun downloadSelectedTheme(selectedThemeUrl: String) {
        viewModelScope.launch {
            Log.i("R/T", "selectedThemeUrl $selectedThemeUrl")
            val themeResource: Resource<String> =
                awsStorageCases.downloadSelectedThemeList(selectedThemeUrl)
            Log.i("R/T", "theme data ${themeResource.data}, theme message ${themeResource.message}")
            _uiMessage.value = R.string.loading

            when (themeResource) {
                is Resource.Success -> {
                    themeResource.data?.let { themeName ->
                        localUserDataStoreCases.saveTheme(themeName)
                        _uiMessage.value = R.string.new_theme_set
                    }
                }

                is Resource.Error -> {
                    _defaultTheme.value = null
                    _uiMessage.value = R.string.new_theme_setting_error
                }
            }
        }
    }

    private fun getUserType() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserType().collectLatest { type ->
                _userType.value = type
                Log.i("R/T", "_userType.value in viewmodel ${_userType.value}")
            }
        }
    }

    fun willShowAd(): Boolean {
        return _userType.value != UserTypeEnum.AD_FREE_USER.type
    }
}