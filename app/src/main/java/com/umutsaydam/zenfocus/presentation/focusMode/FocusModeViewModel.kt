package com.umutsaydam.zenfocus.presentation.focusMode

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository,
) : ViewModel() {
    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)

    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    init {
        getDefaultThemeName()
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