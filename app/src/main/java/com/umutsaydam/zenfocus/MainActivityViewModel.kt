package com.umutsaydam.zenfocus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val readAppEntry: Flow<Boolean> = localUserDataStoreCases.readAppEntry.invoke()

    init {
        startInitialSetupIfFirstEntry()
    }

    private fun startInitialSetupIfFirstEntry() {
        viewModelScope.launch {
            readAppEntry.collect { isEnteredBefore ->
                Log.i("R/T", "isEnteredBefore: $isEnteredBefore")
                if (!isEnteredBefore) {
                    themeRepository.copyDefaultThemeToInternalStorage()
                    saveAppEntry()
                }
            }
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            localUserDataStoreCases.saveAppEntry()
        }
    }
}