package com.umutsaydam.zenfocus.presentation.appLanguage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLanguageViewModel @Inject constructor(
    private val localUserCases: LocalUserCases,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _langList = MutableStateFlow<Array<String>>(
        context.resources.getStringArray(R.array.app_langs)
    )
    private val _defaultLang = MutableStateFlow(_langList.value.first())
    val defaultLang: StateFlow<String> = _defaultLang

    init {
        getDefaultAppLang()
    }

    private fun getDefaultAppLang() {
        viewModelScope.launch {
            localUserCases.readAppLang.invoke().collect { lang ->
                _defaultLang.value = lang
            }
        }
    }

    val langList: StateFlow<Array<String>> = _langList

    fun setDefaultLang(lang: String) {
        if (_defaultLang.value != lang) {
            _defaultLang.value = lang
            saveAppLang(lang)
        }
    }

    private fun getLangCode(): String {
        return _defaultLang.value.split("-")[1]
    }

    private fun saveAppLang(lang: String) {
        viewModelScope.launch {
            localUserCases.saveAppLang.invoke(lang)
        }
    }
}