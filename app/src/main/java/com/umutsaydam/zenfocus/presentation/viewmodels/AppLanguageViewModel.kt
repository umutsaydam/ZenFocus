package com.umutsaydam.zenfocus.presentation.viewmodels

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.LanguageModel
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
    var langList: MutableStateFlow<List<LanguageModel>> = MutableStateFlow(emptyList())

    private val _defaultLang = MutableStateFlow<LanguageModel?>(null)
    val defaultLang: StateFlow<LanguageModel?> = _defaultLang

    init {
        getDefaultAppLang()
    }

    fun updateLangList(currContext: Context) {
        langList.value = listOf(
            LanguageModel("tr", currContext.getString(R.string.turkish_lang)),
            LanguageModel("en", currContext.getString(R.string.english_lang)),
            LanguageModel("zh", currContext.getString(R.string.chinese_lang)),
        )
    }

    private fun getDefaultAppLang() {
        viewModelScope.launch {
            val fullLangName = localUserDataStoreCases.readAppLang().first()
            _defaultLang.value = LanguageModel(getLangCode(fullLangName), fullLangName)
        }
    }

    fun setDefaultLang(lang: String) {
        val langCode = getLangCode(lang)
        val translatedLang = langList.value.find { it.code == langCode } ?: return

        if (_defaultLang.value != translatedLang) {
            _defaultLang.value = translatedLang
            saveAppLang()
            changeAppLang(langCode)
        }
    }


    private fun getLangCode(lang: String): String {
        return lang.substringAfterLast("-", "")
    }

    private fun saveAppLang() {
        viewModelScope.launch {
            _defaultLang.value?.let {
                localUserDataStoreCases.saveAppLang(it.fullName)
            }
        }
    }

    private fun changeAppLang(langCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(langCode)
        } else {
            val config = context.resources.configuration
            val localeList = LocaleList.forLanguageTags(langCode)
            config.setLocales(localeList)
            context.createConfigurationContext(config)
        }
    }
}