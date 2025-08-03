package com.umutsaydam.zenfocus.presentation.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.model.ColorSelectionEnum
import com.umutsaydam.zenfocus.domain.model.ProgressColor
import com.umutsaydam.zenfocus.domain.model.SessionStateEnum
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.ThemeRepositoryUseCases
import com.umutsaydam.zenfocus.util.Constants.IMAGES_FORMATS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorSelectionViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val themeRepositoryUseCases: ThemeRepositoryUseCases
) : ViewModel() {
    /*
        private val _colorList = MutableStateFlow<List<ProgressColor?>>(emptyList())
        val colorList: StateFlow<List<ProgressColor?>> = _colorList

        private fun getColorList() {
            _colorList.value = listOf(null) + ProgressColor.colorList + null
        }
     */
    val colorList: MutableList<ProgressColor?> = mutableListOf(null)

    private val _defaultThemeName = MutableStateFlow<String?>(null)
    val defaultThemeName: StateFlow<String?> = _defaultThemeName

    private val _defaultTheme = MutableStateFlow<Bitmap?>(null)
    val defaultTheme: StateFlow<Bitmap?> = _defaultTheme

    private val _defaultWorkSessionTrackColor = MutableStateFlow<ProgressColor?>(null)
    private val _selectedWorkSessionTrackColor = MutableStateFlow<ProgressColor?>(null)
    private val _defaultWorkSessionTextColor = MutableStateFlow<ProgressColor?>(null)
    private val _selectedWorkSessionTextColor = MutableStateFlow<ProgressColor?>(null)

    private val _defaultBreakSessionTrackColor = MutableStateFlow<ProgressColor?>(null)
    private val _selectedBreakSessionTrackColor = MutableStateFlow<ProgressColor?>(null)
    private val _defaultBreakSessionTextColor = MutableStateFlow<ProgressColor?>(null)
    private val _selectedBreakSessionTextColor = MutableStateFlow<ProgressColor?>(null)

    private val _currentProgressTrackColor = MutableStateFlow<ProgressColor?>(null)
    val currentProgressTrackColor: StateFlow<ProgressColor?> = _currentProgressTrackColor

    private val _currentTextColor = MutableStateFlow<ProgressColor?>(null)
    val currentTextColor: StateFlow<ProgressColor?> = _currentTextColor

    private val _gridStates = ColorSelectionEnum.entries.associateWith { LazyGridState() }

    private val _sessionStateEnum = MutableStateFlow(SessionStateEnum.WORK_SESSION)
    val sessionStateEnum: StateFlow<SessionStateEnum> = _sessionStateEnum

    private val _colorSelectionEnum = MutableStateFlow(ColorSelectionEnum.WORK_TEXT_COLOR)
    val colorSelectionEnum: StateFlow<ColorSelectionEnum> = _colorSelectionEnum

    init {
        getColorList()
        getDefaultWorkSessionTrackColorId()
        getDefaultWorkSessionTextColorId()
        getDefaultBreakSessionTrackColorId()
        getDefaultBreakSessionTextColorId()
        getDefaultThemeName()
    }

    private fun getColorList() {
        colorList.addAll(ProgressColor.colorList)
        colorList.add(null)
    }

    private fun getDefaultThemeName() {
        viewModelScope.launch {
            val defaultThemeName = localUserDataStoreCases.readTheme().first()
            _defaultThemeName.value = defaultThemeName
            getTheme(defaultThemeName)
        }
    }

    private fun getTheme(themeName: String) {
        viewModelScope.launch {
            _defaultTheme.value = themeRepositoryUseCases.getTheme(themeName)
        }
    }

    fun isThemeAnImage(): Boolean {
        return IMAGES_FORMATS.any { _defaultThemeName.value?.lowercase()?.contains(it) ?: false }
    }

    fun setProgressColorSelectionEnum() {
        _colorSelectionEnum.value = if (_sessionStateEnum.value == SessionStateEnum.WORK_SESSION) {
            ColorSelectionEnum.WORK_PROGRESS_COLOR
        } else {
            ColorSelectionEnum.BREAK_PROGRESS_COLOR
        }
    }

    fun setTextColorSelectionEnum() {
        _colorSelectionEnum.value = if (_sessionStateEnum.value == SessionStateEnum.WORK_SESSION) {
            ColorSelectionEnum.WORK_TEXT_COLOR
        } else {
            ColorSelectionEnum.BREAK_TEXT_COLOR
        }
    }

    private fun setSelectedWorkSessionTrackColor(newWorkTrackColorId: ProgressColor) {
        Log.d("R/T", "Work progress color")
        _selectedWorkSessionTrackColor.value = newWorkTrackColorId
        updateProgressColorIfSessionMatches(SessionStateEnum.WORK_SESSION, newWorkTrackColorId)
    }

    private fun saveWorkSessionTrackColor() {
        val newWorkTrackColorId = _selectedWorkSessionTrackColor.value?.colorId
        if (newWorkTrackColorId != null && newWorkTrackColorId != _defaultWorkSessionTrackColor.value?.colorId) {
            viewModelScope.launch {
                localUserDataStoreCases.saveWorkSessionTrackColor(newWorkTrackColorId)
            }
        }
    }

    private fun setDefaultWorkSessionTrackColor(workTrackColorId: Int) {
        val progressColor = ProgressColor.fromId(workTrackColorId)
        _defaultWorkSessionTrackColor.value = progressColor
        _selectedWorkSessionTrackColor.value = progressColor
        updateProgressColorIfSessionMatches(SessionStateEnum.WORK_SESSION, progressColor)
    }

    private fun getDefaultWorkSessionTrackColorId() {
        viewModelScope.launch {
            localUserDataStoreCases.readWorkSessionTrackColor().collectLatest { workTrackColorId ->
                setDefaultWorkSessionTrackColor(workTrackColorId)
            }
        }
    }

    private fun setSelectedBreakSessionTrackColor(newWorkTrackColor: ProgressColor) {
        Log.d("R/T", "Break progress color")
        _selectedBreakSessionTrackColor.value = newWorkTrackColor
        updateProgressColorIfSessionMatches(SessionStateEnum.BREAK_SESSION, newWorkTrackColor)
    }

    private fun saveBreakSessionTrackColor() {
        val newBreakTrackColorId = _selectedBreakSessionTrackColor.value?.colorId
        if (newBreakTrackColorId != null && newBreakTrackColorId != _defaultBreakSessionTrackColor.value?.colorId) {
            viewModelScope.launch {
                localUserDataStoreCases.saveBreakSessionTrackColor(newBreakTrackColorId)
            }
        }
    }

    private fun updateProgressColorIfSessionMatches(
        session: SessionStateEnum,
        newTrackColor: ProgressColor
    ) {
        if (_sessionStateEnum.value == session) {
            _currentProgressTrackColor.value = newTrackColor
        }
    }

    private fun setDefaultBreakSessionTrackColor(breakTrackColorId: Int) {
        val progressColor = ProgressColor.fromId(breakTrackColorId)
        _defaultBreakSessionTrackColor.value = progressColor
        _selectedBreakSessionTrackColor.value = progressColor
    }

    private fun getDefaultBreakSessionTrackColorId() {
        viewModelScope.launch {
            localUserDataStoreCases.readBreakSessionTrackColor()
                .collectLatest { breakTrackColorId ->
                    setDefaultBreakSessionTrackColor(breakTrackColorId)
                }
        }
    }

    private fun setSelectedWorkSessionTextColor(newWorkTextColor: ProgressColor) {
        Log.d("R/T", "Work text color")
        _selectedWorkSessionTextColor.value = newWorkTextColor
        updateTextColorIfSessionMatches(SessionStateEnum.WORK_SESSION, newWorkTextColor)
    }

    private fun saveWorkSessionTextColor() {
        val newWorkTextColor = _selectedWorkSessionTextColor.value
        if (newWorkTextColor != null && newWorkTextColor.colorId != _defaultWorkSessionTextColor.value?.colorId) {
            viewModelScope.launch {
                localUserDataStoreCases.saveWorkSessionTextColor(newWorkTextColor.colorId)
            }
        }
    }

    private fun setDefaultWorkSessionTextColor(workTextColor: Int) {
        val progressColor = ProgressColor.fromId(workTextColor)
        _defaultWorkSessionTextColor.value = progressColor
        _selectedWorkSessionTextColor.value = progressColor
    }

    private fun getDefaultWorkSessionTextColorId() {
        viewModelScope.launch {
            localUserDataStoreCases.readWorkSessionTextColor().collectLatest { workTextColor ->
                setDefaultWorkSessionTextColor(workTextColor)
            }
        }
    }

    private fun setSelectedBreakSessionTextColor(newBreakTextColor: ProgressColor) {
        Log.d("R/T", "Break text color")
        _selectedBreakSessionTextColor.value = newBreakTextColor
        updateTextColorIfSessionMatches(SessionStateEnum.BREAK_SESSION, newBreakTextColor)
    }

    private fun saveBreakSessionTextColor() {
        Log.d("R/T", "saveBreakSessionTextColor: ${_selectedBreakSessionTextColor.value} ${_defaultBreakSessionTextColor.value?.colorId}")
        val newBreakTextColor = _selectedBreakSessionTextColor.value
        if (newBreakTextColor != null && newBreakTextColor.colorId != _defaultBreakSessionTextColor.value?.colorId) {
            Log.d("R/T", "saveBreakSessionTextColor if")
            viewModelScope.launch {
                localUserDataStoreCases.saveBreakSessionTextColor(newBreakTextColor.colorId)
            }
        }
    }

    private fun setDefaultBreakSessionTextColor(breakTextColor: Int) {
        val progressColor = ProgressColor.fromId(breakTextColor)
        _defaultBreakSessionTextColor.value = progressColor
        _selectedBreakSessionTextColor.value = progressColor
    }

    private fun updateTextColorIfSessionMatches(
        session: SessionStateEnum,
        newTextColor: ProgressColor
    ) {
        if (_sessionStateEnum.value == session) {
            _currentTextColor.value = newTextColor
        }
    }

    private fun getDefaultBreakSessionTextColorId() {
        viewModelScope.launch {
            localUserDataStoreCases.readBreakSessionTextColor().collectLatest { breakTextColor ->
                setDefaultBreakSessionTextColor(breakTextColor)
            }
        }
    }

    fun setSessionState(newSessionState: SessionStateEnum) {
        _sessionStateEnum.value = newSessionState
    }

    fun setCurrentProgressTrackColorBySessionState(sessionState: SessionStateEnum) {
        _currentProgressTrackColor.value = if (sessionState == SessionStateEnum.WORK_SESSION) {
            _selectedWorkSessionTrackColor.value
        } else {
            _selectedBreakSessionTrackColor.value
        }
    }

    fun setCurrentTextColorBySessionState(sessionState: SessionStateEnum) {
        _currentTextColor.value = if (sessionState == SessionStateEnum.WORK_SESSION) {
            _selectedWorkSessionTextColor.value
        } else {
            _selectedBreakSessionTextColor.value
        }
    }

    fun setColorByEnumValue(color: ProgressColor) {
        when (_colorSelectionEnum.value) {
            ColorSelectionEnum.WORK_PROGRESS_COLOR -> setSelectedWorkSessionTrackColor(color)
            ColorSelectionEnum.WORK_TEXT_COLOR -> setSelectedWorkSessionTextColor(color)
            ColorSelectionEnum.BREAK_PROGRESS_COLOR -> setSelectedBreakSessionTrackColor(color)
            ColorSelectionEnum.BREAK_TEXT_COLOR -> setSelectedBreakSessionTextColor(color)
        }
    }

    fun getGridStateForSession(sessionState: ColorSelectionEnum): LazyGridState {
        Log.d("R/T", "getGridStateForSession")
        return _gridStates[sessionState] ?: LazyGridState()
    }

    fun saveAllTrackColors() {
        saveWorkSessionTrackColor()
        saveBreakSessionTrackColor()
        saveWorkSessionTextColor()
        saveBreakSessionTextColor()
    }
}