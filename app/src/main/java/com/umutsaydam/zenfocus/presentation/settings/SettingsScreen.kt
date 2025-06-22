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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.BaseListItem
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
import com.umutsaydam.zenfocus.ui.theme.Primary
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
                    settingsViewModel.savePomodoroWorkDuration()
                } else {
                    settingsViewModel.savePomodoroBreakDuration()
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

                if (isSelected) {
                    if (isWorkDurationDialogOpened) {
                        settingsViewModel.updateWorkDuration(currIndex)
                    } else {
                        settingsViewModel.updateBreakDuration(currIndex)
                    }
                }
            },
            onDismissRequest = {
                coroutine.launch {
                    if (isWorkDurationDialogOpened) {
                        workGridState.scrollToItem(maxOf(0, uiState.pomodoroWorkDuration - 1))
                    } else {
                        breakGridState.scrollToItem(maxOf(0, uiState.pomodoroBreakDuration - 1))
                    }
                    isWorkDurationDialogOpened = false
                    isDurationDialogOpened = false
                }
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
                    BaseListItem(
                        title = stringResource(R.string.appearance),
                        description = "Select a theme for the focus mode",
                        onClick = { navController.safeNavigate(Route.Appearance.route) },
                        iconRes = R.drawable.ic_theme,
                        contentDesc = "Select a theme for the focus mode"
                    )
                    BaseListItem(
                        title = stringResource(R.string.change_the_app_language),
                        description = "Change the app language",
                        onClick = { navController.safeNavigate(Route.AppLanguage.route) },
                        iconRes = R.drawable.ic_translate,
                        contentDesc = "Change the app language"
                    )
                    BaseListItem(
                        title = stringResource(R.string.vibrate),
                        onClick = { settingsViewModel.setVibrateState(!uiState.defaultVibrateState) },
                        iconRes = R.drawable.ic_vibrate,
                        contentDesc = stringResource(R.string.vibrate),
                        trailingContent = {
                            Switch(
                                modifier = Modifier
                                    .scale(0.7f)
                                    .weight(0.2f),
                                checked = uiState.defaultVibrateState,
                                onCheckedChange = { newState ->
                                    settingsViewModel.setVibrateState(newState)
                                },
                                colors = SwitchDefaults.colors().copy(
                                    checkedThumbColor = Primary,
                                    checkedTrackColor = LightGray,
                                    uncheckedThumbColor = LightGray,
                                    uncheckedTrackColor = Primary,
                                )
                            )
                        }
                    )
                    BaseListItem(
                        title = stringResource(R.string.time_out_ringer),
                        description = "When session ends, play a ringtone",
                        onClick = { },
                        iconRes = R.drawable.ic_bell,
                        contentDesc = stringResource(R.string.time_out_ringer),
                        trailingContent = {
                            Switch(
                                modifier = Modifier
                                    .scale(0.7f)
                                    .weight(0.2f),
                                checked = uiState.defaultTimeOutRingerState,
                                onCheckedChange = { newState ->
                                    settingsViewModel.setTimeOutRingerState(newState)
                                },
                                colors = SwitchDefaults.colors().copy(
                                    checkedThumbColor = Primary,
                                    checkedTrackColor = LightGray,
                                    uncheckedThumbColor = LightGray,
                                    uncheckedTrackColor = Primary,
                                )
                            )
                        }
                    )
                    BaseListItem(
                        title = stringResource(R.string.working_time),
                        description = "Set working session duration",
                        onClick = {
                            isWorkDurationDialogOpened = true
                            isDurationDialogOpened = true
                        },
                        iconRes = R.drawable.ic_study,
                        contentDesc = stringResource(R.string.working_time)
                    )
                    BaseListItem(
                        title = stringResource(R.string.break_time),
                        description = "Set working session duration",
                        onClick = {
                            isWorkDurationDialogOpened = false
                            isDurationDialogOpened = true
                        },
                        iconRes = R.drawable.ic_coffee,
                        contentDesc = stringResource(R.string.break_time)
                    )
                    BaseListItem(
                        title = stringResource(R.string.statistics),
                        description = "View your pomodoro statistics",
                        onClick = { navController.safeNavigate(Route.Statistics.route) },
                        iconRes = R.drawable.ic_statistics,
                        contentDesc = stringResource(R.string.statistics)
                    )
                }
            )
            SettingsSection(
                content = {
                    BaseListItem(
                        title = stringResource(R.string.policy),
                        onClick = { navController.safeNavigate(Route.Policy.route) },
                        iconRes = R.drawable.ic_policy,
                        contentDesc = stringResource(R.string.policy)
                    )
                    BaseListItem(
                        title = stringResource(R.string.version),
                        onClick = {},
                        iconRes = R.drawable.ic_package,
                        contentDesc = stringResource(R.string.version),
                        trailingContent = { Text(stringResource(R.string.version_number)) }
                    )
                }
            )
            SettingsSection(
                content = {
                    if (isSignedInState) {
                        BaseListItem(
                            title = stringResource(R.string.log_out),
                            onClick = {
                                settingsViewModel.signOut()
                                navController.popBackStackOrIgnore()
                            },
                            iconRes = R.drawable.ic_log_out,
                            contentDesc = stringResource(R.string.log_out)
                        )
                    } else {
                        BaseListItem(
                            title = stringResource(R.string.sign_in_sign_up),
                            onClick = { navController.safeNavigate(Route.Auth.route) },
                            iconRes = R.drawable.ic_account,
                            contentDesc = stringResource(R.string.sign_in_sign_up)
                        )
                    }
                }
            )
        }
    }
}