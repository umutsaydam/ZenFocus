package com.umutsaydam.zenfocus.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.viewmodels.StatisticsViewModel
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {
    val options = listOf("Select dates", "This month", "Last week", "This week")
    var selectedIndex by remember { mutableIntStateOf(options.size - 1) }

    val totalStatisticsUiState by statisticsViewModel.totalStatisticsUiState.collectAsState()
    val statisticsByDate by statisticsViewModel.statisticsByDateUiState.collectAsState()
    val rememberScrollState = rememberScrollState()
    var dateRangeState by remember { mutableStateOf(false) }

    LaunchedEffect(selectedIndex) {
        when (selectedIndex) {
            0 -> {
                if (!dateRangeState) {
                    dateRangeState = true
                }
            }

            1 -> statisticsViewModel.getThisMonthCompletedPomodoroDataset()
            2 -> statisticsViewModel.getLastWeekCompletedPomodoroDataset()
            3 -> statisticsViewModel.getThisWeekCompletedPomodoroDataset()
            else -> statisticsViewModel.resetPomodoroData()
        }
    }

    if (dateRangeState) {
        val dateRangePickerState = rememberDateRangePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )
        CustomDateRangePicker(
            dateRangePickerState = dateRangePickerState,
            onDateRangeStateChange = { state ->
                dateRangeState = state
            },
            onSelectedDates = { startDate, endDate ->
                statisticsViewModel.getPomodoroDatasetBySpecificDate(startDate, endDate)
            }
        )
    }

    StatusBarSwitcher(true)

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(
            color = SurfaceContainerLow
        ),
        containerColor = Transparent,
        topBar = { StatisticsScreenTopBar(navController) }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding,
                    bottom = bottomPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PADDING_MEDIUM2),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(CORNER_MEDIUM))
                        .clip(RoundedCornerShape(CORNER_MEDIUM))
                        .background(White)
                        .fillMaxWidth()
                        .padding(vertical = PADDING_SMALL),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TotalStatisticsSection(
                        totalCountOfPomodoro = totalStatisticsUiState.countOfTotalPomodoro.toString(),
                        contentTotalCountOfPomodoro = "Total Pomodoro",
                        countOfCurrentStreak = totalStatisticsUiState.currentStreak.toString(),
                        contentOfCurrentStreak = "Current Streak",
                        countOfLongestStreak = totalStatisticsUiState.longestStreak.toString(),
                        contentOfLongestStreak = "Longest Streak"
                    )
                }
            }

            SingleChoiceSegmentedButtons(
                rememberScrollState = rememberScrollState,
                options = options,
                selectedIndex = selectedIndex,
                onSelectedIndexChanged = { newIndex ->
                    selectedIndex = newIndex

                    if (newIndex == 0 && !dateRangeState) {
                        dateRangeState = true
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PADDING_MEDIUM2),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(CORNER_MEDIUM))
                        .clip(RoundedCornerShape(CORNER_MEDIUM))
                        .background(White)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomRoundedBarChartWithDataset(
                        numberOfCompletedPomodoroDataset = statisticsByDate.totalMinutes,
                        datesOfCompletedPomodoroDataset = statisticsByDate.dates
                    )

                    Text(
                        modifier = Modifier.padding(PADDING_SMALL2),
                        text = "Bu hafta en çok çalıştığın saat 09:00 - 10:00 \uD83D\uDD25",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = Outline
                        )
                    )
                }
            }

        }
    }
}

@Composable
fun StatisticsScreenTopBar(navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }

    IconWithTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStackOrIgnore()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.back_to_settings),
                    tint = Outline
                )
            }
        },
        containerColor = SurfaceContainerLow,
        actions = {
            IconButton(
                onClick = { isExpanded = !isExpanded }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Outline
                )
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Back up to cloud") },
                    onClick = {
                        // perform backing up to cloud...
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_cloud),
                            contentDescription = "Back up to cloud",
                            tint = Outline
                        )
                    }
                )
            }
        }
    )
}