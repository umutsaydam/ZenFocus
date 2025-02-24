package com.umutsaydam.zenfocus.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.ThemeRepositoryUseCases
import com.umutsaydam.zenfocus.util.Constants.IMAGES_FORMATS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepositoryUseCases: ThemeRepositoryUseCases
) : ViewModel() {
    private val _themeName = MutableStateFlow<String?>(null)
    val themeName: StateFlow<String?> = _themeName

    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)
    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    init {
        getDefaultThemeName()
    }

    private fun getDefaultThemeName() {
        viewModelScope.launch {
            val defaultThemeName = localUserDataStoreCases.readTheme()
            defaultThemeName.collect { themeName ->
                _themeName.value = themeName
                getTheme(themeName)
            }
        }
    }

    private fun getTheme(themeName: String) {
        viewModelScope.launch {
            val theme = themeRepositoryUseCases.getTheme(themeName)
            _defaultTheme.value = theme
        }
    }

    fun isThemeAnImage(): Boolean {
        return IMAGES_FORMATS.any { _themeName.value?.lowercase()?.contains(it) ?: false }
    }
}