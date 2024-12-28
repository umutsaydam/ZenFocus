package com.umutsaydam.zenfocus.presentation.appLanguage

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AppLanguageViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
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
            localUserDataStoreCases.readAppLang.invoke().collect { lang ->
                _defaultLang.value = lang
            }
        }
    }

    val langList: StateFlow<Array<String>> = _langList

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
            localUserDataStoreCases.saveAppLang.invoke(_defaultLang.value)
        }
    }

    private fun changeAppLang() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.i("R/T", "tiramisu > ${getLangCode()}")
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(getLangCode())
        } else {
            Log.i("R/T", "tiramisu < ${getLangCode()}")
            val config = context.resources.configuration
            val localeList = LocaleList.forLanguageTags(getLangCode())
            config.setLocales(localeList)
            context.createConfigurationContext(config)
        }
    }
}