package com.umutsaydam.zenfocus.presentation.home

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.policy.RadioButtonWithText

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val toDoList = listOf(
        "Study Algorithm",
        "Read a book",
        "Review the codes"
    )

    val soundList = listOf(
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
    var defaultSound by remember {
        mutableStateOf("None")
    }
    var selectedSound by remember {
        mutableStateOf("None")
    }

    var bottomSheetState by remember {
        mutableStateOf(false)
    }

    var currentSheetContent by remember {
        mutableStateOf<BottomSheetContent?>(null)
    }

    var sliderPosition by remember {
        mutableFloatStateOf(1f)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RectangleShape
            )
    ) {
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painterResource(R.drawable.lofi1),
//            contentDescription = "Selected theme",
//            contentScale = ContentScale.Fit
//        )

        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                IconWithTopAppBar(
                    title = {

                    },
                    containerColor = Color.Transparent,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                //:TODO perform navigate
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = stringResource(R.string.open_side_menu),
                                tint = Color.White
                            )
                        }
                    }
                ) {

                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressWithText(
                        progress = 0.6f,
                        text = "25:00"
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
                    FocusControlButtons(
                        onClick = {
                            currentSheetContent = BottomSheetContent.PomodoroTimes
                            bottomSheetState = true
                        },
                        painterResource = painterResource(R.drawable.ic_time),
                        contentDescription = stringResource(R.string.pomodoro_times)
                    )
                    FocusControlButtons(
                        onClick = {
                            currentSheetContent = BottomSheetContent.FocusMode
                            bottomSheetState = true
                        },
                        painterResource = painterResource(R.drawable.ic_play_arrow),
                        contentDescription = stringResource(R.string.pomodoro_start)
                    )
                    FocusControlButtons(
                        onClick = {
                            currentSheetContent = BottomSheetContent.PomodoroSounds
                            bottomSheetState = true
                        },
                        painterResource = painterResource(R.drawable.ic_music),
                        contentDescription = stringResource(R.string.pomodoro_sounds)
                    )
                }

                LazyToDoList(
                    toDoList = toDoList
                ) { index ->
                    ToDoListItem(
                        toDoTitle = toDoList[index],
                        onClick = {

                        }
                    )
                }

                CustomFab(
                    alignment = Alignment.BottomEnd,
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    fabIcon = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_to_do_button),
                    onClick = {
                        currentSheetContent = BottomSheetContent.AddToDo
                        bottomSheetState = true
                    }
                )

                if (bottomSheetState) {
                    CustomBottomSheet(
                        onDismissRequest = {
                            bottomSheetState = false
                        },
                        content = {
                            when (currentSheetContent) {
                                BottomSheetContent.AddToDo -> {
                                    AddToDo { newTask ->
                                        //TODO add newTask
                                    }
                                }

                                BottomSheetContent.PomodoroSounds -> {
                                    LazySoundList(
                                        soundList = soundList,
                                        content = { index ->
                                            RadioButtonWithText(
                                                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                                radioSelected = false,
                                                radioText = soundList[index],
                                                onClick = {
                                                    selectedSound = soundList[index]
                                                    //:TODO play sound
                                                }
                                            )
                                        },
                                        fixedContent = { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = PADDING_MEDIUM2),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Button(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(BUTTON_HEIGHT_MEDIUM),
                                                    onClick = {
                                                        defaultSound = soundList[index]
                                                    },
                                                    enabled = defaultSound != selectedSound
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.select)
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }

                                BottomSheetContent.PomodoroTimes -> {
                                    PomodoroControlSlider(
                                        sliderPosition = 1f,
                                        steps = 2,
                                        valueRange = 1f..4f
                                    ) { newSliderPosition ->
                                        sliderPosition = newSliderPosition
                                    }
                                }

                                BottomSheetContent.FocusMode -> {

                                }

                                null -> Unit
                            }
                        }
                    )
                }
            }
        }
    }
}