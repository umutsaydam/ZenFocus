package com.umutsaydam.zenfocus.presentation.appearance

import androidx.lifecycle.ViewModel
import com.umutsaydam.zenfocus.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor() : ViewModel() {
    private val _themeList = MutableStateFlow<List<Int?>>(
        listOf(
            null,
            R.drawable.tomato,
            R.drawable.tomato,
            R.drawable.tomato,
            R.drawable.tomato,
            R.drawable.tomato,
            R.drawable.tomato,
            null
        )
    )
    val themeList: StateFlow<List<Int?>> = _themeList

    private val _defaultTheme = MutableStateFlow<Int>(R.drawable.lofi1)
    val defaultTheme: StateFlow<Int?> = _defaultTheme

    fun setDefaultTheme(newTheme: Int?) {
        if (newTheme != null && defaultTheme.value != newTheme) {
            _defaultTheme.value = newTheme
        }
    }
}