package com.umutsaydam.zenfocus.presentation.appLanguage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppLanguageViewModel @Inject constructor() : ViewModel() {
    private val _langList = MutableStateFlow<List<String>>(
        listOf(
            "Türkçe",
            "İngilizce"
        )
    )
    val langList: StateFlow<List<String>> = _langList

    private val _defaultLang = MutableStateFlow<String>("Türkçe")
    val defaultLang: StateFlow<String> = _defaultLang

    fun setDefaultLang(lang: String) {
        if (_defaultLang.value != lang) {
            _defaultLang.value = lang
        }
    }
}