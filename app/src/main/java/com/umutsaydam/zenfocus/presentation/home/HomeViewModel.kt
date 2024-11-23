package com.umutsaydam.zenfocus.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _toDoList = MutableStateFlow<List<String>>(
        listOf(
            "Study Algorithm",
            "Read a book",
            "Review the codes"
        )
    )
    val toDoList: StateFlow<List<String>> = _toDoList

    private val _soundList = MutableStateFlow<List<String>>(
        listOf(
            "None",
            "LoFi Rainy",
            "Lorem Ipsum",
            "Dolor Lorem",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit",
            "Param Ipsum",
            "Donec ut est id color malesuada",
            "Donec eget maximus elit1"
        )
    )
    val soundList: StateFlow<List<String>> = _soundList

    private val _defaultSound = MutableStateFlow<String>("defaultSound")
    val defaultSound = _defaultSound

    fun setDefaultSound(newSound: String) {
        if (_defaultSound.value != newSound) {
            _defaultSound.value = newSound
        }
    }

    private val _sliderPosition = MutableStateFlow<Float>(1f)
    val sliderPosition = _sliderPosition

    fun setSliderPosition(position: Float) {
        _sliderPosition.value = position
    }

    private val _bottomSheetContent = MutableStateFlow<BottomSheetContent?>(null)
    val bottomSheetContent: StateFlow<BottomSheetContent?> = _bottomSheetContent

    private fun setBottomSheetContent(content: BottomSheetContent) {
        _bottomSheetContent.value = content
    }

    private val _bottomSheetState = MutableStateFlow<Boolean>(false)
    val bottomSheetState: StateFlow<Boolean> = _bottomSheetState

    fun setBottomSheetState(state: Boolean) {
        _bottomSheetState.value = state
    }

    fun showPomodoroTimesBottomSheet(){
        setBottomSheetContent(BottomSheetContent.PomodoroTimes)
        setBottomSheetState(true)
    }

    fun showPomodoroSoundsBottomSheet(){
        setBottomSheetContent(BottomSheetContent.PomodoroSounds)
        setBottomSheetState(true)
    }

    fun showAddToDoBottomSheet() {
        setBottomSheetContent(BottomSheetContent.AddToDo)
        setBottomSheetState(true)
    }
}