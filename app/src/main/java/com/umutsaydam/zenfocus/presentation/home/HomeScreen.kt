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
import androidx.compose.ui.graphics.Color
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
import com.umutsaydam.zenfocus.presentation.policy.components.RadioButtonWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.Log
import com.google.android.gms.ads.AdSize
import com.umutsaydam.zenfocus.presentation.viewmodels.PomodoroViewModel
import com.umutsaydam.zenfocus.domain.model.TaskModel
import com.umutsaydam.zenfocus.presentation.common.CustomAlertDialog
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.home.components.AddToDo
import com.umutsaydam.zenfocus.presentation.home.components.BottomSheetContent
import com.umutsaydam.zenfocus.presentation.common.CircularProgressWithText
import com.umutsaydam.zenfocus.presentation.home.components.CustomBottomSheet
import com.umutsaydam.zenfocus.presentation.home.components.CustomFab
import com.umutsaydam.zenfocus.presentation.home.components.FocusControlButtons
import com.umutsaydam.zenfocus.presentation.home.components.LazySoundList
import com.umutsaydam.zenfocus.presentation.home.components.LazyToDoList
import com.umutsaydam.zenfocus.presentation.home.components.PomodoroControlSlider
import com.umutsaydam.zenfocus.presentation.home.components.ToDoListItem
import com.umutsaydam.zenfocus.presentation.viewmodels.GoogleBannerAdState
import com.umutsaydam.zenfocus.presentation.viewmodels.HomeUiState
import com.umutsaydam.zenfocus.presentation.viewmodels.HomeViewModel
import com.umutsaydam.zenfocus.presentation.viewmodels.IntegrateInAppReviewViewModel
import com.umutsaydam.zenfocus.ui.theme.OutLineVariant
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.Primary
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.util.safeNavigate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    pomodoroViewModel: PomodoroViewModel = hiltViewModel(),
    reviewViewModel: IntegrateInAppReviewViewModel = hiltViewModel()
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val pomodoroUiState by pomodoroViewModel.pomodoroUiState.collectAsState()
    val soundList by homeViewModel.focusSoundList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteTaskDialog by remember { mutableStateOf(false) }
    var selectedTaskModel: TaskModel? = null
    val context = LocalContext.current
    val adState by homeViewModel.adState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigationEvent by homeViewModel.navigationEvent.collectAsState()
    val currentProgressTrackColor by pomodoroViewModel.currentProgressTrackColor.collectAsState()
    val currentTextColor by pomodoroViewModel.currentTextColor.collectAsState()

    LaunchedEffect(homeUiState.uiMessage) {
        homeUiState.uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            homeViewModel.clearUiMessage()
        }
    }

    LaunchedEffect(navigationEvent) {
        when(navigationEvent){
            Route.Auth -> { navController.safeNavigate(Route.Auth.route) }
            else -> {}
        }
        homeViewModel.clearNavigationEvent()
    }

    ObserverLifecycleEvents(
        lifecycleOwner = lifecycleOwner, viewModel = pomodoroViewModel
    )

    StatusBarSwitcher(true)

    if (homeUiState.bottomSheetState) {
        Box(modifier = Modifier.fillMaxSize()) {
            CustomBottomSheet(onDismissRequest = {
                homeViewModel.setBottomSheetState(false)
                if (!pomodoroUiState.isTimerRunning) {
                    homeViewModel.stopSound()
                }
            }, content = {
                BottomSheetContentHandler(
                    homeUiState = homeUiState,
                    viewModel = homeViewModel,
                    soundList = soundList
                )
            })
        }
    }

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(
            color = SurfaceContainerLow
        ), containerColor = Transparent, topBar = {
        HomeAppBar(
            navController = navController, viewModel = homeViewModel
        )
    }) { paddingValues ->

        StopPomodoroDialog(showDialog = showDialog) { confirmedState ->
            if (confirmedState) {
                pomodoroViewModel.stopTimer()
                if (homeViewModel.isNetworkConnected() && reviewViewModel.isAvailableForReview.value) {
                    reviewViewModel.launchReview(context)
                }
            }
            showDialog = false
        }

        DeleteTaskDialog(
            showDialog = showDeleteTaskDialog, taskModel = selectedTaskModel
        ) { confirmedState ->
            if (confirmedState) {
                homeViewModel.deleteTask(selectedTaskModel!!)
            }
            showDeleteTaskDialog = false
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.d("R/T", "${currentProgressTrackColor == null} ${currentTextColor == null}")
            if(currentProgressTrackColor != null && currentTextColor != null){
                HomeCircularProgress(
                    remainingPercent = pomodoroUiState.remainingPercent,
                    remainingTime = pomodoroUiState.remainingTime,
                    isWorking = pomodoroUiState.isWorkingSession,
                    progressColor = currentProgressTrackColor!!.color,
                    textColor = currentTextColor!!.color
                )
            }

            Spacer(modifier = Modifier.height(SPACE_MEDIUM))

            FocusControlButtonGroup(isTimerRunning = pomodoroUiState.isTimerRunning,
                navController = navController,
                pomodoroViewModel = pomodoroViewModel,
                homeViewModel = homeViewModel,
                onShowDialog = { showDialog = true })

            HomeToDoList(toDoList = homeUiState.toDoList, onClick = { newTaskModel ->
                homeViewModel.upsertTask(newTaskModel)
            }, onLongClick = { selectedTask ->
                selectedTaskModel = selectedTask
                showDeleteTaskDialog = true
            })

            HomeFab(
                viewModel = homeViewModel, adState = adState
            )

            BannerAdView(
                viewModel = homeViewModel, adState = adState
            )
        }
    }
}

@Composable
fun ObserverLifecycleEvents(
    lifecycleOwner: LifecycleOwner, viewModel: PomodoroViewModel
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.setTimer()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun BottomSheetContentHandler(
    homeUiState: HomeUiState, viewModel: HomeViewModel, soundList: Array<String>
) {
    when (homeUiState.bottomSheetContent) {
        BottomSheetContent.AddToDo -> {
            AddToDo { newTask ->
                viewModel.upsertTask(newTask)
            }
        }

        BottomSheetContent.PomodoroSounds -> {
            LazySoundList(soundList = soundList, content = { index ->
                val sound = soundList[index]
                RadioButtonWithText(
                    radioSelected = sound == homeUiState.defaultSound,
                    radioText = sound,
                    onClick = {
                        viewModel.setDefaultSoundAndPlay(sound)
                    })
            }, fixedContent = { index -> })
        }

        BottomSheetContent.PomodoroTimes -> {
            PomodoroControlSlider(
                sliderPosition = homeUiState.sliderPosition, steps = 2, valueRange = 1f..4f
            ) { newSliderPosition ->
                viewModel.setSliderPosition(newSliderPosition)
                viewModel.setBottomSheetState(false)
            }
        }

        null -> Unit
    }
}

@Composable
fun HomeAppBar(
    navController: NavHostController, viewModel: HomeViewModel
) {
    val context = LocalContext.current

    IconWithTopAppBar(title = {

    }, containerColor = Transparent, navigationIcon = {
        IconButton(onClick = {
            navController.safeNavigate(Route.Settings.route)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = stringResource(R.string.open_side_menu),
                tint = Outline
            )
        }
    }, actions = {
        if (viewModel.shouldShowAd()) {
            TextButton(onClick = {
                val activity = context as? Activity
                activity?.let {
                    viewModel.startProductsInApp(it)
                }
            }) {
                Text(
                    text = stringResource(R.string.remove_ad),
                    color = Primary
                )
            }
        }
    })
}

@Composable
fun StopPomodoroDialog(
    showDialog: Boolean, isConfirmed: (Boolean) -> Unit
) {
    if (showDialog) {
        CustomAlertDialog(icon = painterResource(R.drawable.ic_timer_off),
            title = stringResource(R.string.stop_pomodoro),
            text = stringResource(R.string.pomodoro_will_stop),
            isConfirmed = { confirmedState ->
                isConfirmed(confirmedState)
            })
    }
}

@Composable
fun DeleteTaskDialog(
    showDialog: Boolean, taskModel: TaskModel?, isConfirmed: (Boolean) -> Unit
) {
    if (showDialog && taskModel != null) {
        CustomAlertDialog(icon = painterResource(R.drawable.ic_delete),
            title = stringResource(R.string.delete_task),
            text = stringResource(R.string.task_will_delete),
            isConfirmed = { confirmedState ->
                isConfirmed(confirmedState)
            })
    }
}

@Composable
fun FocusControlButtonGroup(
    isTimerRunning: Boolean,
    navController: NavHostController,
    pomodoroViewModel: PomodoroViewModel,
    homeViewModel: HomeViewModel,
    onShowDialog: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
            PADDING_SMALL, Alignment.CenterHorizontally
        )
    ) {
        if (isTimerRunning) {
            FocusControlButtons(
                onClick = { pomodoroViewModel.pauseTimer() },
                painterResource = painterResource(R.drawable.ic_pause_black),
                contentDescription = stringResource(R.string.pomodoro_pause)
            )
            FocusControlButtons(
                onClick = { onShowDialog() },
                painterResource = painterResource(R.drawable.ic_timer_off),
                contentDescription = stringResource(R.string.pomodoro_stop)
            )
        } else {
            FocusControlButtons(
                onClick = { homeViewModel.showPomodoroTimesBottomSheet() },
                painterResource = painterResource(R.drawable.ic_time),
                contentDescription = stringResource(R.string.pomodoro_times)
            )
            FocusControlButtons(
                onClick = { pomodoroViewModel.playOrResumeTimer() },
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
        if (isTimerRunning) {
            FocusControlButtons(
                onClick = { navController.safeNavigate(Route.FocusMode.route) },
                painterResource = painterResource(R.drawable.ic_focus_black),
                contentDescription = stringResource(R.string.back_to_focus_mode)
            )
        }
    }
}


@Composable
fun HomeCircularProgress(
    remainingPercent: Float,
    remainingTime: String,
    isWorking: Boolean,
    progressColor: Color,
    textColor: Color
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressWithText(
            progress = remainingPercent,
            isWorking = isWorking,
            progressColor = progressColor,
            text = remainingTime,
            textColor = textColor
        )
    }
}

@Composable
fun HomeToDoList(
    toDoList: List<TaskModel>, onClick: (TaskModel) -> Unit, onLongClick: (TaskModel) -> Unit
) {
    LazyToDoList(
        toDoList = toDoList
    ) { index ->
        val currTaskModel = toDoList[index]
        ToDoListItem(taskModel = currTaskModel, onClick = { newState ->
            onClick(currTaskModel.copy(isTaskCompleted = newState))
        }, onLongClick = { taskModel ->
            onLongClick(taskModel)
        })
    }
}

@Composable
fun HomeFab(
    viewModel: HomeViewModel, adState: GoogleBannerAdState
) {
    Box(
        modifier = if (adState.isAdLoaded) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        CustomFab(alignment = Alignment.BottomEnd,
            containerColor = OutLineVariant,
            fabIcon = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(R.string.add_to_do_button),
            onClick = {
                viewModel.showAddToDoBottomSheet()
            })
    }
}

@Composable
fun BannerAdView(
    viewModel: HomeViewModel, adState: GoogleBannerAdState
) {
    if (viewModel.isNetworkConnected() && viewModel.shouldShowAd() && (adState.isAdLoaded || !adState.isFirstAdRequested)) {
        val context = LocalContext.current
        val adSize: AdSize by remember { derivedStateOf { getAdSize(context) } }

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
                viewModel.showBannerAd(adSize)
            })
        }
    }
}

private fun getAdSize(context: Context): AdSize {
    val activity = context as? Activity
    activity?.let {
        val displayMetrics = context.resources.displayMetrics
        val adWithPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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