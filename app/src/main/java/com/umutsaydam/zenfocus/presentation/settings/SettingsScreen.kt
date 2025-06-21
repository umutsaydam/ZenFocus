package com.umutsaydam.zenfocus.presentation.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.presentation.settings.components.MenuItem
import com.umutsaydam.zenfocus.presentation.settings.components.MenuItemDescription
import com.umutsaydam.zenfocus.presentation.settings.components.MenuItemSwitch
import com.umutsaydam.zenfocus.presentation.settings.components.NumberPickerDialog
import com.umutsaydam.zenfocus.presentation.settings.components.SettingsSection
import com.umutsaydam.zenfocus.presentation.viewmodels.SettingsViewModel
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val uiState by settingsViewModel.uiState.collectAsState()
    val workGridState = rememberLazyListState()
    val breakGridState = rememberLazyListState()
    var isDurationDialogOpened by remember { mutableStateOf(false) }
    var isWorkDurationDialogOpened by remember { mutableStateOf(true) }
    val isSignedInState by remember { derivedStateOf { uiState.isSignedInState } }

    if (isDurationDialogOpened) {
        val currentGridState = if (isWorkDurationDialogOpened) workGridState else breakGridState
        LaunchedEffect(key1 = true, isWorkDurationDialogOpened) {
            coroutine.launch {
                delay(200)
                currentGridState.animateScrollToItem(
                    if (isWorkDurationDialogOpened) {
                        Log.d("R/T", "work section ${uiState.pomodoroWorkDuration - 1}")
                        uiState.pomodoroWorkDuration - 1
                    } else {
                        Log.d("R/T", "break section ${uiState.pomodoroBreakDuration - 1}")
                        uiState.pomodoroBreakDuration - 1
                    }
                )
            }
        }
        NumberPickerDialog(
            textTitle = stringResource(
                if (isWorkDurationDialogOpened)
                    R.string.working_time
                else
                    R.string.break_time
            ),
            gridState = currentGridState,
            onClick = {
                if (isWorkDurationDialogOpened) {
                    settingsViewModel.savePomodoroWorkDuration(uiState.pomodoroWorkDuration)
                } else {
                    settingsViewModel.savePomodoroBreakDuration(uiState.pomodoroBreakDuration)
                }
            },
            content = { visible, currIndex ->
                val isSelected = visible + 1 == currIndex
                Text(
                    text = currIndex.toString(),
                    fontSize = if (isSelected) 24.sp else 16.sp,
                    color = if (isSelected) Gray else LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            coroutine.launch {
                                currentGridState.animateScrollToItem(
                                    index = maxOf(0, currIndex - 1)
                                )
                            }
                        },
                    textAlign = TextAlign.Center
                )
            },
            onDismissRequest = {
                coroutine.launch {
                    if (isWorkDurationDialogOpened) {
                        workGridState.scrollToItem(maxOf(0, uiState.pomodoroWorkDuration - 1))
                    } else {
                        Log.d("R/T", "break triggered")
                        breakGridState.scrollToItem(maxOf(0, uiState.pomodoroBreakDuration - 1))
                    }
                    isWorkDurationDialogOpened = false
                }
                isDurationDialogOpened = false
            }
        )
    }

    StatusBarSwitcher()

    Scaffold(
        modifier = modifier,
        containerColor = SurfaceContainerLow,
        topBar = {
            IconWithTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings),
                        color = Outline
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackOrIgnore()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back_to_home),
                            tint = Outline
                        )
                    }
                }
            ) {

            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)
        ) {
            SettingsSection(
                content = {
                    MenuItem(
                        menuTitle = stringResource(R.string.appearance),
                        onClick = {
                            navController.safeNavigate(Route.Appearance.route)
                        }
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.change_the_app_language),
                        onClick = {
                            navController.safeNavigate(Route.AppLanguage.route)
                        }
                    )
                    MenuItemSwitch(
                        menuTitle = stringResource(R.string.vibrate),
                        onClick = { newState ->
                            settingsViewModel.setVibrateState(newState)
                        },
                        isChecked = uiState.defaultVibrateState
                    )
                    MenuItemSwitch(
                        menuTitle = stringResource(R.string.time_out_ringer),
                        onClick = { newState ->
                            settingsViewModel.setTimeOutRingerState(newState)
                        },
                        isChecked = uiState.defaultTimeOutRingerState
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.working_time),
                        onClick = {
                            isWorkDurationDialogOpened = true
                            isDurationDialogOpened = true
                        }
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.break_time),
                        onClick = {
                            isWorkDurationDialogOpened = false
                            isDurationDialogOpened = true
                        }
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.statistics),
                        onClick = {
                            navController.safeNavigate(Route.Statistics.route)
                        }
                    )
                }
            )
            SettingsSection(
                content = {
                    MenuItem(
                        menuTitle = stringResource(R.string.policy),
                        onClick = {
                            navController.safeNavigate(Route.Policy.route)
                        }
                    )
                    MenuItemDescription(
                        menuTitle = stringResource(R.string.version),
                        description = stringResource(R.string.version_number),
                        onClick = {
                            //TODO: perform onClick
                        }
                    )
                }
            )
            SettingsSection(
                content = {
                    if (isSignedInState) {
                        MenuItem(
                            headIcon = R.drawable.ic_log_out,
                            menuTitle = stringResource(R.string.log_out),
                            onClick = {
                                settingsViewModel.signOut()
                                navController.popBackStackOrIgnore()
                            }
                        )
                    } else {
                        MenuItem(
                            headIcon = R.drawable.ic_account,
                            menuTitle = stringResource(R.string.sign_in_sign_up),
                            onClick = {
                                navController.safeNavigate(Route.Auth.route)
                            }
                        )
                    }
                }
            )
        }
    }
}