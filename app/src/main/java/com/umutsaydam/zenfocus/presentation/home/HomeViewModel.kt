package com.umutsaydam.zenfocus.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val toDoUsesCases: ToDoUsesCases
) : ViewModel() {
    private val _toDoList = MutableStateFlow<List<TaskModel>>(emptyList())
    val toDoList: StateFlow<List<TaskModel>> = _toDoList

    init {
        getTasks()
    }

    private fun getTasks() {
        viewModelScope.launch {
            _toDoList.value = toDoUsesCases.getTasks.invoke()
        }
    }

    fun upsertTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.upsertTask.invoke(taskModel)
            getTasks()
        }
    }

    fun deleteTask(taskModel: TaskModel) {
        viewModelScope.launch {
            toDoUsesCases.deleteTask.invoke(taskModel)
            getTasks()
        }
    }

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

    fun showPomodoroTimesBottomSheet() {
        setBottomSheetContent(BottomSheetContent.PomodoroTimes)
        setBottomSheetState(true)
    }

    fun showPomodoroSoundsBottomSheet() {
        setBottomSheetContent(BottomSheetContent.PomodoroSounds)
        setBottomSheetState(true)
    }

    fun showAddToDoBottomSheet() {
        setBottomSheetContent(BottomSheetContent.AddToDo)
        setBottomSheetState(true)
    }
}