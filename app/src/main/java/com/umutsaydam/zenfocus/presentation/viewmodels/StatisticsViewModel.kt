package com.umutsaydam.zenfocus.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : ViewModel() {
    private val _numberOfPomodoroDataset: MutableStateFlow<List<Float>> =
        MutableStateFlow(emptyList())
    val numberOfPomodoroDataset: StateFlow<List<Float>> = _numberOfPomodoroDataset

    private val _completedPomodoroDates: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val completedPomodoroDates: StateFlow<List<String>> = _completedPomodoroDates

    init {
        Log.i("R/T", "initialized!!!")
    }

    fun getThisWeekCompletedPomodoroDataset() {
        Log.i("R/T", "called getThisWeekCompletedPomodoroDataset")
        _numberOfPomodoroDataset.value = listOf(4f, 2f, 3f, 2f, 4f, 3f, 4f)
        _completedPomodoroDates.value = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    fun getLastWeekCompletedPomodoroDataset() {
        Log.i("R/T", "called getLastWeekCompletedPomodoroDataset")
        _numberOfPomodoroDataset.value = listOf(1f, 2f, 3f, 2f, 1f, 0f, 4f)
        _completedPomodoroDates.value = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    fun getThisMonthCompletedPomodoroDataset() {
        Log.i("R/T", "called getThisMonthCompletedPomodoroDataset")
        _numberOfPomodoroDataset.value = listOf(
            20f, 10f, 5f, 8f, 12f, 16f,
            11f, 7f, 12f, 15f, 14f, 9f
        )
        _completedPomodoroDates.value = listOf(
            "Jan", "Feb", "Mar", "Apr", "May",
            "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
        )
    }

    fun getPomodoroDatasetBySpecificDate(startDate: String, endDate: String) {
        Log.i("R/T", "StartDate: $startDate - EndDate: $endDate")
        _numberOfPomodoroDataset.value = listOf(
            1f
        )
        _completedPomodoroDates.value = listOf(
            "Mon"
        )
    }

    fun resetPomodoroData() {
        _numberOfPomodoroDataset.value = emptyList()
        _completedPomodoroDates.value = emptyList()
    }
}