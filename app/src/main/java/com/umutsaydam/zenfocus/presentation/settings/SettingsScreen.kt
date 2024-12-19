package com.umutsaydam.zenfocus.presentation.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
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
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val vibrateState = settingsViewModel.defaultVibrateState.collectAsState()
    val isSignedInState = settingsViewModel.isSignedInState.collectAsState()
    val coroutine = rememberCoroutineScope()
    val pomodoroWorkDuration by settingsViewModel.pomodoroWorkDuration.collectAsState()
    val pomodoroBreakDuration by settingsViewModel.pomodoroBreakDuration.collectAsState()
    val selectedWorkDuration = mutableStateOf(pomodoroWorkDuration)
    val selectedBreakDuration = mutableStateOf(pomodoroBreakDuration)
    val gridState = rememberLazyListState()
    var isDurationDialogOpened by remember { mutableStateOf(false) }
    var isWorkDurationDialogOpened by remember { mutableStateOf(true) }

    if (isDurationDialogOpened) {
        coroutine.launch {
            gridState.animateScrollToItem(
                if (isWorkDurationDialogOpened) {
                    selectedWorkDuration.value - 1
                } else {
                    selectedBreakDuration.value - 1
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
                Log.i(
                    "R/T",
                    "selectedWorkDuration: ${selectedWorkDuration.value} & selectedBreakDuration: ${selectedBreakDuration.value}"
                )
                if (isWorkDurationDialogOpened) {
                    settingsViewModel.savePomodoroWorkDuration(selectedWorkDuration.value)
                } else {
                    settingsViewModel.savePomodoroBreakDuration(selectedBreakDuration.value)
                }

                isDurationDialogOpened = false
            },
            content = { visible, currIndex ->
                val isSelected = visible + 1 == currIndex
                Text(
                    text = currIndex.toString(),
                    fontSize = if (isSelected) 24.sp else 16.sp,
                    color = if (isSelected) Color.Gray else Color.LightGray,
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
                Log.i("R/T", "Selected duration $currIndex")
                if (isSelected) {
                    if (isWorkDurationDialogOpened) {
                        selectedWorkDuration.value = currIndex
                    } else {
                        selectedBreakDuration.value = currIndex
                    }
                }
            },
            onDismissRequest = {
                isWorkDurationDialogOpened = false
                isDurationDialogOpened = false
                isDurationDialogOpened = false
            }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            IconWithTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings),
                        color = MaterialTheme.colorScheme.outline
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
                            tint = MaterialTheme.colorScheme.outline
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
            Spacer(modifier = Modifier.height(SPACE_MEDIUM))
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
                        isChecked = vibrateState.value
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
                        description = "1.2",
                        onClick = {
                            //TODO: perform onClick
                        }
                    )
                }
            )
            SettingsSection(
                content = {
                    if (isSignedInState.value) {
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

@Composable
fun SettingsSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        content()
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