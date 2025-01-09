package com.umutsaydam.zenfocus.presentation.appLanguage

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLanguageViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val langList: List<String> = context.resources.getStringArray(R.array.app_langs).toList()

    private val _defaultLang = MutableStateFlow(langList.first())
    val defaultLang: StateFlow<String> = _defaultLang

    init {
        getDefaultAppLang()
    }

    private fun getDefaultAppLang() {
        viewModelScope.launch {
            _defaultLang.value = localUserDataStoreCases.readAppLang().first()
        }
    }

    fun setDefaultLang(lang: String) {
        if (_defaultLang.value != lang) {
            _defaultLang.value = lang
            saveAppLang()
            changeAppLang()
        }
    }

    private fun getLangCode(): String {
        return _defaultLang.value.split("-")[1]
    }

    private fun saveAppLang() {
        viewModelScope.launch {
            localUserDataStoreCases.saveAppLang(_defaultLang.value)
        }
    }

    private fun changeAppLang() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(getLangCode())
        } else {
            val config = context.resources.configuration
            val localeList = LocaleList.forLanguageTags(getLangCode())
            config.setLocales(localeList)
            context.createConfigurationContext(config)
        }
    }
}