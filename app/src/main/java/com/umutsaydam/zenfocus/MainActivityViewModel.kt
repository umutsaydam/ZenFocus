package com.umutsaydam.zenfocus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.util.Constants.APP_LANG_CHINESE
import com.umutsaydam.zenfocus.util.Constants.APP_LANG_ENGLISH
import com.umutsaydam.zenfocus.util.Constants.APP_LANG_TURKISH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val readAppEntry: Flow<Boolean> = localUserDataStoreCases.readAppEntry()
    val defaultAppLang: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        getDefaultAppLang()
    }

    fun startInitialSetupIfFirstEntry(currentLocale: Locale) {
        viewModelScope.launch {
            readAppEntry.collect { isEnteredBefore ->
                if (!isEnteredBefore) {
                    themeRepository.copyDefaultThemeToInternalStorage()
                    saveAppEntry()

                    saveAppLang(currentLocale)
                }
            }
        }
    }

    private fun saveAppLang(currentLocale: Locale) {
        viewModelScope.launch {
            when (currentLocale.language) {
                "tr" -> localUserDataStoreCases.saveAppLang(APP_LANG_TURKISH)
                "en" -> localUserDataStoreCases.saveAppLang(APP_LANG_ENGLISH)
                "zh" -> localUserDataStoreCases.saveAppLang(APP_LANG_CHINESE)
                else -> localUserDataStoreCases.saveAppLang(APP_LANG_ENGLISH)
            }
        }
    }

    private fun getDefaultAppLang() {
        viewModelScope.launch {
            localUserDataStoreCases.readAppLang().collect { langWithCode ->
                val langCode = langWithCode.split("-")[1]
                defaultAppLang.value = langCode
            }
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            localUserDataStoreCases.saveAppEntry()
        }
    }
}