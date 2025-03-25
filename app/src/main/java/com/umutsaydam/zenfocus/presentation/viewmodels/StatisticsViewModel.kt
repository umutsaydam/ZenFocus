package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TotalStatisticsUiState(
    val countOfTotalPomodoro: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val pomodoroSessionsUseCases: PomodoroSessionsUseCases
) : ViewModel() {
    private val _totalStatisticsUiState = MutableStateFlow(TotalStatisticsUiState())
    val totalStatisticsUiState: StateFlow<TotalStatisticsUiState> = _totalStatisticsUiState

    private val _numberOfPomodoroDataset: MutableStateFlow<List<Float>> =
        MutableStateFlow(emptyList())
    val numberOfPomodoroDataset: StateFlow<List<Float>> = _numberOfPomodoroDataset

    private val _completedPomodoroDates: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val completedPomodoroDates: StateFlow<List<String>> = _completedPomodoroDates

    init {
        getCountOfTotalPomodoro()
        getCurrentStreak()
        getLongestStreak()
    }

    private fun updateTotalStatisticsUiState(update: TotalStatisticsUiState.() -> TotalStatisticsUiState) {
        _totalStatisticsUiState.value = _totalStatisticsUiState.value.update()
    }

    private fun getCountOfTotalPomodoro() {
        viewModelScope.launch {
            val total = pomodoroSessionsUseCases.getCountOfTotalPomodoroSessions()
            updateTotalStatisticsUiState { copy(countOfTotalPomodoro = total) }
        }
    }

    private fun getCurrentStreak() {
        viewModelScope.launch {
            val curStreak = pomodoroSessionsUseCases.getCurrentStreak()
            updateTotalStatisticsUiState { copy(currentStreak = curStreak) }
        }
    }

    private fun getLongestStreak() {
        viewModelScope.launch {
            val longest = pomodoroSessionsUseCases.getLongestStreak()
            updateTotalStatisticsUiState { copy(longestStreak = longest) }
        }
    }

    fun getThisWeekCompletedPomodoroDataset() {
        _numberOfPomodoroDataset.value = listOf(4f, 2f, 3f, 2f, 4f, 3f, 4f)
        _completedPomodoroDates.value = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    fun getLastWeekCompletedPomodoroDataset() {
        _numberOfPomodoroDataset.value = listOf(1f, 2f, 3f, 2f, 1f, 0f, 4f)
        _completedPomodoroDates.value = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    fun getThisMonthCompletedPomodoroDataset() {
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