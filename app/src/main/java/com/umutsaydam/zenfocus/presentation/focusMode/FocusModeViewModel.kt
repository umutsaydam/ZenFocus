package com.umutsaydam.zenfocus.presentation.focusMode

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val localUserCases: LocalUserCases,
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)
    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    private val _remainTime = MutableStateFlow<String>("25:00")
    val remainTime: StateFlow<String> = _remainTime

    init {
        getDefaultThemeName()
    }

    private fun getDefaultThemeName() {
        viewModelScope.launch {
            val defaultThemeName = localUserCases.readTheme()
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

    fun playTimer() {
        //TODO play timer
    }

    fun stopTimer() {
        //TODO stop timer
    }
}