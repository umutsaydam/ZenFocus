package com.umutsaydam.zenfocus.presentation.statistics

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.presentation.statistics.components.CustomDateRangePicker
import com.umutsaydam.zenfocus.presentation.statistics.components.CustomRoundedBarChartWithDataset
import com.umutsaydam.zenfocus.presentation.statistics.components.NoStatistics
import com.umutsaydam.zenfocus.presentation.statistics.components.SingleChoiceSegmentedButtons
import com.umutsaydam.zenfocus.presentation.statistics.components.TotalStatisticsSection
import com.umutsaydam.zenfocus.presentation.viewmodels.StatisticsViewModel
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {
    val options: List<String> = listOf(
        stringResource(R.string.select_dates),
        stringResource(R.string.this_month),
        stringResource(R.string.last_week),
        stringResource(R.string.this_week)
    )
    var selectedIndex by remember { mutableIntStateOf(options.size - 1) }

    val totalStatisticsUiState by statisticsViewModel.totalStatisticsUiState.collectAsState()
    val statisticsByDate by statisticsViewModel.statisticsByDateUiState.collectAsState()
    val uiMessage by statisticsViewModel.uiMessage.collectAsState()
    val rememberScrollState = rememberScrollState()
    var dateRangeState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(selectedIndex, uiMessage) {
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

        uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            if (message == R.string.first_login_to_back_up || message == R.string.first_login_to_synchronize) {
                navController.safeNavigate(Route.Auth.route)
            }
            statisticsViewModel.clearUiMessage()
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
        topBar = {
            StatisticsScreenTopBar(
                navController = navController,
                statisticsViewModel = statisticsViewModel
            )
        }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()

        if (totalStatisticsUiState.countOfTotalPomodoro != 0) {
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
                            contentTotalCountOfPomodoro = stringResource(R.string.total_pomodoro),
                            countOfCurrentStreak = totalStatisticsUiState.currentStreak.toString(),
                            contentOfCurrentStreak = stringResource(R.string.current_streak),
                            countOfLongestStreak = totalStatisticsUiState.longestStreak.toString(),
                            contentOfLongestStreak = stringResource(R.string.longest_streak)
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
                    if(statisticsByDate.totalMinutes.isNotEmpty()){
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
                        }
                    }else{
                        Text(
                            text = stringResource(R.string.not_found_data),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = LightGray,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        } else {
            NoStatistics()
        }
    }
}

@Composable
fun StatisticsScreenTopBar(
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel
) {
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
                    contentDescription = stringResource(R.string.more_options),
                    tint = Outline
                )
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.back_up_to_cloud)) },
                    onClick = {
                        isExpanded = false
                        statisticsViewModel.backupPomodoroSessions()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_cloud),
                            contentDescription = stringResource(R.string.back_up_to_cloud),
                            tint = Outline
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.synchronize_data)) },
                    onClick = {
                        isExpanded = false
                        statisticsViewModel.synchronizePomodoroSessions()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_cloud_sync),
                            contentDescription = stringResource(R.string.synchronize_data),
                            tint = Outline
                        )
                    }
                )
            }
        }
    )
}