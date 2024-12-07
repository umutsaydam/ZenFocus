package com.umutsaydam.zenfocus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import com.umutsaydam.zenfocus.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localUserCases: LocalUserCases,
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val readAppEntry: Flow<Boolean> = localUserCases.readAppEntry.invoke()

    init {
        startInitialSetupIfFirstEntry()
    }

    private fun startInitialSetupIfFirstEntry() {
        viewModelScope.launch {
            readAppEntry.collect { isEnteredBefore ->
                Log.i("R/T", "isEnteredBefore: $isEnteredBefore")
                if (!isEnteredBefore) {
                    themeRepository.copyDefaultThemeToInternalStorage()
                    setDefaultTheme()
                    saveAppEntry()
                }
            }
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            localUserCases.saveAppEntry()
        }
    }

    private fun setDefaultTheme() {
        viewModelScope.launch {
            localUserCases.saveTheme(Constants.DEFAULT_THEME)
        }
    }
}