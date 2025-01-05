package com.umutsaydam.zenfocus.presentation.home

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.umutsaydam.zenfocus.BuildConfig
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
    val remainingTime by homeViewModel.remainingTime.collectAsState()
    val remainingPercent by homeViewModel.remainingPercent.collectAsState()
    val toDoList by homeViewModel.toDoList.collectAsState()
    val isTimerRunning by homeViewModel.isTimerRunning.collectAsState()
    val soundList by homeViewModel.focusSoundList.collectAsState()
    val defaultSound by homeViewModel.defaultSound.collectAsState()
    val bottomSheetState = homeViewModel.bottomSheetState.collectAsState()
    val currentSheetContent by homeViewModel.bottomSheetContent.collectAsState()
    val sliderPosition by homeViewModel.sliderPosition.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteTaskDialog by remember { mutableStateOf(false) }
    var selectedTaskModel: TaskModel? = null
    val context = LocalContext.current
    var isFirstAdRequested by remember { mutableStateOf(false) }
    var isAdLoaded by remember { mutableStateOf(false) }
    val adSize: AdSize = getAdSize(context)
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiMessage by homeViewModel.uiMessage.collectAsState()

    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
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
                    if (homeViewModel.willShowAd()) {
                        TextButton(
                            onClick = {
                                Log.i("R/AD", "Button clicked...")
                                homeViewModel.startProductsInApp()
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
                    progress = remainingPercent,
                    text = remainingTime
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
                if (isTimerRunning) {
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
                        homeViewModel.showPomodoroSoundsBottomSheet()
                    },
                    painterResource = painterResource(R.drawable.ic_music),
                    contentDescription = stringResource(R.string.pomodoro_sounds)
                )
                if (isTimerRunning) {
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
                toDoList = toDoList
            ) { index ->
                val currTaskModel = toDoList[index]
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
                modifier = if (isAdLoaded) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
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

            if (homeViewModel.isNetworkConnected() && homeViewModel.willShowAd() && (isAdLoaded || !isFirstAdRequested)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            AdView(context).apply {
                                setAdSize(adSize)
                                adUnitId = BuildConfig.AD_BANNER_UNIT_ID
                                loadAd(AdRequest.Builder().build())

                                adListener = object : AdListener() {
                                    override fun onAdClicked() {
                                        Log.i("A/D", "onAdClicked")
                                    }

                                    override fun onAdClosed() {
                                        super.onAdClosed()
                                        Log.i("A/D", "onAdClosed")
                                    }

                                    override fun onAdFailedToLoad(p0: LoadAdError) {
                                        super.onAdFailedToLoad(p0)
                                        Log.i("A/D", "onAdFailedToLoad $p0")
                                        isAdLoaded = false
                                        isFirstAdRequested = true
                                    }

                                    override fun onAdImpression() {
                                        super.onAdImpression()
                                        Log.i("A/D", "onAdImpression")
                                    }

                                    override fun onAdLoaded() {
                                        super.onAdLoaded()
                                        Log.i("A/D", "onAdLoaded")
                                        isAdLoaded = true
                                        isFirstAdRequested = true
                                    }

                                    override fun onAdOpened() {
                                        super.onAdOpened()
                                        Log.i("A/D", "onAdOpened")
                                    }

                                    override fun onAdSwipeGestureClicked() {
                                        super.onAdSwipeGestureClicked()
                                        Log.i("A/D", "onAdSwipeGestureClicked")
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (bottomSheetState.value) {
                CustomBottomSheet(
                    onDismissRequest = {
                        homeViewModel.setBottomSheetState(false)
                        if (!isTimerRunning) {
                            homeViewModel.stopSound()
                        }
                    },
                    content = {
                        when (currentSheetContent) {
                            BottomSheetContent.AddToDo -> {
                                AddToDo { newTask ->
                                    homeViewModel.upsertTask(newTask)
                                }
                            }

                            BottomSheetContent.PomodoroSounds -> {
                                LazySoundList(
                                    soundList = soundList,
                                    content = { index ->
                                        val sound = soundList[index]
                                        RadioButtonWithText(
                                            modifier = Modifier.background(White),
                                            radioSelected = sound == defaultSound,
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
                                    sliderPosition = sliderPosition,
                                    steps = 2,
                                    valueRange = 1f..4f
                                ) { newSliderPosition ->
                                    homeViewModel.setSliderPosition(newSliderPosition)
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