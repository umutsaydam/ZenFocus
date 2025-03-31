package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsPomodoroSessionsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
    private val pomodoroSessionsUseCases: PomodoroSessionsUseCases,
    private val awsPomodoroSessionsUseCases: AwsPomodoroSessionsUseCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val checkerUseCases: NetworkCheckerUseCases,
) : ViewModel() {
    private val _totalStatisticsUiState = MutableStateFlow(TotalStatisticsUiState())
    val totalStatisticsUiState: StateFlow<TotalStatisticsUiState> = _totalStatisticsUiState

    private val _statisticsByDateUiState = MutableStateFlow(StatisticsByDate())
    val statisticsByDateUiState: StateFlow<StatisticsByDate> = _statisticsByDateUiState

    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormatUI = SimpleDateFormat("dd MMM E", Locale.getDefault())

    private val _uiMessage = MutableStateFlow<Int?>(null)
    val uiMessage: StateFlow<Int?> = _uiMessage

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

    fun backupPomodoroSessions() {
        viewModelScope.launch {
            if (checkerUseCases.isConnected()) {
                localUserDataStoreCases.readUserId().collectLatest { userId ->
                    if (userId.isNotEmpty()) {
                        val allData = pomodoroSessionsUseCases.getAllSessions()
                        if(allData.isNotEmpty()){
                            val result =
                                awsPomodoroSessionsUseCases.backupPomodoroSessions(userId, allData)

                            when (result) {
                                is Resource.Success -> {
                                    _uiMessage.value = R.string.backed_up_successfully
                                }

                                is Resource.Error -> {
                                    _uiMessage.value = R.string.could_not_backed_up
                                }
                            }
                        }else{
                            _uiMessage.value = R.string.no_data_to_back_up
                        }
                    } else {
                        _uiMessage.value = R.string.first_login_to_back_up
                    }
                }
            } else {
                _uiMessage.value = R.string.no_connection
            }
        }
    }

    fun synchronizePomodoroSessions() {
        viewModelScope.launch {
            localUserDataStoreCases.readUserId().collectLatest { userId ->
                if (checkerUseCases.isConnected()) {
                    if (userId.isNotEmpty()) {
                        val result = awsPomodoroSessionsUseCases.synchronizePomodoroSessions(userId)

                        when (result) {
                            is Resource.Success -> {
                                _uiMessage.value = R.string.synchronization_successfully
                            }

                            is Resource.Error -> {
                                _uiMessage.value = R.string.could_not_synchronization
                            }
                        }
                    } else {
                        _uiMessage.value = R.string.first_login_to_synchronize
                    }
                } else {
                    _uiMessage.value = R.string.no_connection
                }
            }
        }
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }
}