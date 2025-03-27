package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class TotalStatisticsUiState(
    val countOfTotalPomodoro: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

data class StatisticsByDate(
    val dates: List<String> = emptyList(),
    val totalMinutes: List<Float> = emptyList()
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val pomodoroSessionsUseCases: PomodoroSessionsUseCases
) : ViewModel() {
    private val _totalStatisticsUiState = MutableStateFlow(TotalStatisticsUiState())
    val totalStatisticsUiState: StateFlow<TotalStatisticsUiState> = _totalStatisticsUiState

    private val _statisticsByDateUiState = MutableStateFlow(StatisticsByDate())
    val statisticsByDateUiState: StateFlow<StatisticsByDate> = _statisticsByDateUiState

    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormatUI = SimpleDateFormat("dd MMM E", Locale.getDefault())

    init {
        getCountOfTotalPomodoro()
        getCurrentStreak()
        getLongestStreak()
    }

    private fun updateTotalStatisticsUiState(update: TotalStatisticsUiState.() -> TotalStatisticsUiState) {
        _totalStatisticsUiState.value = _totalStatisticsUiState.value.update()
    }

    private fun updateStatisticsByDate(update: StatisticsByDate.() -> StatisticsByDate) {
        _statisticsByDateUiState.value = _statisticsByDateUiState.value.update()
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
        viewModelScope.launch {
            val thisWeek =
                pomodoroSessionsUseCases.getThisWeekStatistics(dateFormatDB.format(Date()))
            val dates = thisWeek.mapNotNull { formatPomodoroDateUI(it.pomodoroDate) }
            val totalMinutes = thisWeek.map { it.minute.toFloat() }
            updateStatisticsByDate { copy(dates = dates, totalMinutes = totalMinutes) }
        }
    }

    fun getLastWeekCompletedPomodoroDataset() {
        viewModelScope.launch {
            val lastWeek =
                pomodoroSessionsUseCases.getLastWeekStatistics(dateFormatDB.format(Date()))
            val dates = lastWeek.mapNotNull { formatPomodoroDateUI(it.pomodoroDate) }
            val totalMinutes = lastWeek.map { it.minute.toFloat() }
            updateStatisticsByDate { copy(dates = dates, totalMinutes = totalMinutes) }
        }
    }

    fun getThisMonthCompletedPomodoroDataset() {
        viewModelScope.launch {
            val pomodoroData =
                pomodoroSessionsUseCases.getThisMonthStatistics(dateFormatDB.format(Date()))
            val dates =
                pomodoroData.mapNotNull { formatPomodoroDateUI(it.pomodoroDate) }
            val totalMinutes = pomodoroData.map { it.minute.toFloat() }
            updateStatisticsByDate { copy(dates = dates, totalMinutes = totalMinutes) }
        }
    }

    fun getPomodoroDatasetBySpecificDate(startDate: String, endDate: String) {
        viewModelScope.launch {
            val list = pomodoroSessionsUseCases.getCountOfSessionsBetween2Dates(startDate, endDate)
            val dates = list.mapNotNull { formatPomodoroDateUI(it.pomodoroDate) }
            val totalMinutes = list.map { it.minute.toFloat() }
            updateStatisticsByDate { copy(dates = dates, totalMinutes = totalMinutes) }
        }
    }

    fun resetPomodoroData() {
        updateStatisticsByDate { copy(dates = emptyList(), totalMinutes = emptyList()) }
    }

    private fun formatPomodoroDateUI(pomodoroDate: String): String? {
        val date = dateFormatDB.parse(pomodoroDate)
        return if (date != null) dateFormatUI.format(date) else null
    }
}