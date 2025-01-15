package com.umutsaydam.zenfocus.presentation.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.ui.theme.Gray
import com.umutsaydam.zenfocus.ui.theme.LightGray
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val uiState by settingsViewModel.uiState.collectAsState()
    val gridState = rememberLazyListState()
    var isDurationDialogOpened by remember { mutableStateOf(false) }
    var isWorkDurationDialogOpened by remember { mutableStateOf(true) }

    if (isDurationDialogOpened) {
        coroutine.launch {
            gridState.animateScrollToItem(
                if (isWorkDurationDialogOpened) {
                    uiState.pomodoroWorkDuration - 1
                } else {
                    uiState.pomodoroBreakDuration - 1
                }
            )
        }
        NumberPickerDialog(
            textTitle = stringResource(
                if (isWorkDurationDialogOpened)
                    R.string.working_time
                else
                    R.string.break_time
            ),
            gridState = gridState,
            onClick = {
                if (isWorkDurationDialogOpened) {
                    settingsViewModel.savePomodoroWorkDuration(uiState.pomodoroWorkDuration)
                } else {
                    settingsViewModel.savePomodoroBreakDuration(uiState.pomodoroBreakDuration)
                }

                isDurationDialogOpened = false
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
                                gridState.animateScrollToItem(
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
                isWorkDurationDialogOpened = false
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
                .padding(paddingValue),
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
                        description = "1.0",
                        onClick = {
                            //TODO: perform onClick
                        }
                    )
                }
            )
            SettingsSection(
                content = {
                    if (uiState.isSignedInState) {
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

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun SettingsScreenPreview(modifier: Modifier = Modifier) {
    SettingsScreen(navController = rememberNavController())
}