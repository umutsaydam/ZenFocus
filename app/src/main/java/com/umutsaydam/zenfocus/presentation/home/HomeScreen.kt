package com.umutsaydam.zenfocus.presentation.home

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowMetrics
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.presentation.policy.RadioButtonWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.ads.AdSize
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.presentation.common.CustomAlertDialog
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.ui.theme.DarkBackground
import com.umutsaydam.zenfocus.ui.theme.OutLineVariant
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.safeNavigate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val soundList by remember { derivedStateOf { homeViewModel.focusSoundList } }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteTaskDialog by remember { mutableStateOf(false) }
    var selectedTaskModel: TaskModel? = null
    val context = LocalContext.current
    val adState by homeViewModel.adState.collectAsState()
    val adSize: AdSize by remember { derivedStateOf { getAdSize(context) } }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(homeUiState.uiMessage) {
        homeUiState.uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            homeViewModel.clearUiMessage()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    homeViewModel.setTimer()
                }

                Lifecycle.Event.ON_STOP -> {
                    homeViewModel.startPomodoroService()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    StatusBarSwitcher(false)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = DarkBackground
            ),
        containerColor = Transparent,
        topBar = {
            IconWithTopAppBar(
                title = {

                },
                containerColor = Transparent,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.safeNavigate(Route.Settings.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = stringResource(R.string.open_side_menu),
                            tint = White
                        )
                    }
                },
                actions = {
                    if (homeViewModel.shouldShowAd()) {
                        TextButton(
                            onClick = {
                                val activity = context as? Activity
                                activity?.let {
                                    homeViewModel.startProductsInApp(it)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.remove_ad)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        if (showDialog) {
            CustomAlertDialog(
                icon = painterResource(R.drawable.ic_timer_off),
                title = stringResource(R.string.stop_pomodoro),
                text = stringResource(R.string.pomodoro_will_stop),
                isConfirmed = { confirmedState ->
                    if (confirmedState) {
                        homeViewModel.stopTimer()
                    }
                    showDialog = false
                }
            )
        }

        if (showDeleteTaskDialog && selectedTaskModel != null) {
            CustomAlertDialog(
                icon = painterResource(R.drawable.ic_delete),
                title = stringResource(R.string.delete_task),
                text = stringResource(R.string.task_will_delete),
                isConfirmed = { confirmedState ->
                    if (confirmedState) {
                        homeViewModel.deleteTask(selectedTaskModel!!)
                    }
                    showDeleteTaskDialog = false
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressWithText(
                    progress = homeUiState.remainingPercent,
                    text = homeUiState.remainingTime
                )
            }
            Spacer(modifier = Modifier.height(SPACE_MEDIUM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    PADDING_SMALL,
                    Alignment.CenterHorizontally
                )
            ) {
                if (homeUiState.isTimerRunning) {
                    FocusControlButtons(
                        onClick = {
                            homeViewModel.pauseTimer()
                        },
                        painterResource = painterResource(R.drawable.ic_pause_black),
                        contentDescription = stringResource(R.string.pomodoro_pause)
                    )
                    FocusControlButtons(
                        onClick = {
                            showDialog = true
                        },
                        painterResource = painterResource(R.drawable.ic_timer_off),
                        contentDescription = stringResource(R.string.pomodoro_stop)
                    )
                } else {
                    FocusControlButtons(
                        onClick = {
                            homeViewModel.showPomodoroTimesBottomSheet()
                        },
                        painterResource = painterResource(R.drawable.ic_time),
                        contentDescription = stringResource(R.string.pomodoro_times)
                    )
                    FocusControlButtons(
                        onClick = {
                            homeViewModel.playOrResumeTimer()
                        },
                        painterResource = painterResource(R.drawable.ic_play_arrow_black),
                        contentDescription = stringResource(R.string.pomodoro_start)
                    )
                }
                FocusControlButtons(
                    onClick = {
                        homeViewModel.getSoundList()
                        homeViewModel.showPomodoroSoundsBottomSheet()
                    },
                    painterResource = painterResource(R.drawable.ic_music),
                    contentDescription = stringResource(R.string.pomodoro_sounds)
                )
                if (homeUiState.isTimerRunning) {
                    FocusControlButtons(
                        onClick = {
                            navController.safeNavigate(Route.FocusMode.route)
                        },
                        painterResource = painterResource(R.drawable.ic_focus_black),
                        contentDescription = stringResource(R.string.back_to_focus_mode)
                    )
                }
            }

            LazyToDoList(
                toDoList = homeUiState.toDoList
            ) { index ->
                val currTaskModel = homeUiState.toDoList[index]
                ToDoListItem(
                    taskModel = currTaskModel,
                    onClick = { newState ->
                        homeViewModel.upsertTask(currTaskModel.copy(isTaskCompleted = newState))
                    },
                    onLongClick = { taskModel ->
                        selectedTaskModel = taskModel
                        showDeleteTaskDialog = true
                    }
                )
            }

            Box(
                modifier = if (adState.isAdLoaded) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                CustomFab(
                    alignment = Alignment.BottomEnd,
                    containerColor = OutLineVariant,
                    fabIcon = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_to_do_button),
                    onClick = {
                        homeViewModel.showAddToDoBottomSheet()
                    }
                )
            }

            if (homeViewModel.isNetworkConnected() && homeViewModel.shouldShowAd() && (adState.isAdLoaded || !adState.isFirstAdRequested)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            homeViewModel.showBannerAd(adSize)
                        }
                    )
                }
            }

            if (homeUiState.bottomSheetState) {
                CustomBottomSheet(
                    onDismissRequest = {
                        homeViewModel.setBottomSheetState(false)
                        if (!homeUiState.isTimerRunning) {
                            homeViewModel.stopSound()
                        }
                    },
                    content = {
                        when (homeUiState.bottomSheetContent) {
                            BottomSheetContent.AddToDo -> {
                                AddToDo { newTask ->
                                    homeViewModel.upsertTask(newTask)
                                }
                            }

                            BottomSheetContent.PomodoroSounds -> {
                                LazySoundList(
                                    soundList = soundList.value,
                                    content = { index ->
                                        val sound = soundList.value[index]
                                        RadioButtonWithText(
                                            modifier = Modifier.background(White),
                                            radioSelected = sound == homeUiState.defaultSound,
                                            radioText = sound,
                                            onClick = {
                                                homeViewModel.setDefaultSoundAndPlay(sound)
                                            }
                                        )
                                    },
                                    fixedContent = { index -> }
                                )
                            }

                            BottomSheetContent.PomodoroTimes -> {
                                PomodoroControlSlider(
                                    sliderPosition = homeUiState.sliderPosition,
                                    steps = 2,
                                    valueRange = 1f..4f
                                ) { newSliderPosition ->
                                    homeViewModel.setSliderPosition(newSliderPosition)
                                    homeViewModel.setBottomSheetState(false)
                                }
                            }

                            null -> Unit
                        }
                    }
                )
            }
        }
    }
}

private fun getAdSize(context: Context): AdSize {
    val activity = context as? Activity
    activity?.let {
        val displayMetrics = context.resources.displayMetrics
        val adWithPixels =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics: WindowMetrics = it.window.windowManager.currentWindowMetrics
                windowMetrics.bounds.width()
            } else {
                displayMetrics.widthPixels
            }
        val density = displayMetrics.density
        val adWidth = (adWithPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }
    return AdSize.BANNER
}