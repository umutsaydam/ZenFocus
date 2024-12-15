package com.umutsaydam.zenfocus.presentation.appearance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsStorageCases
import com.umutsaydam.zenfocus.util.FileNameFromUrl
import com.umutsaydam.zenfocus.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val awsStorageCases: AwsStorageCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _themeList = MutableStateFlow<List<ThemeInfo?>>(listOf(null))
    val themeList: StateFlow<List<ThemeInfo?>> = _themeList

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        getThemeList()
    }

    private val _defaultTheme = MutableStateFlow<ThemeInfo?>(null)
    val defaultTheme: StateFlow<ThemeInfo?> = _defaultTheme

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
        viewModelScope.launch {
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
                    _errorMessage.value = themesResult.message
                    Log.i("R/T", "Error in viewmodel: ${themesResult.message}")
                }

                is Resource.Loading -> {
                    Log.i("R/T", "loading...")
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

            when (themeResource) {
                is Resource.Success -> {
                    themeResource.data?.let { themeName ->
                        localUserDataStoreCases.saveTheme(themeName)
                    }
                }

                is Resource.Error -> {
                    _defaultTheme.value = null
                    _errorMessage.value = themeResource.message
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}