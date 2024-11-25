package com.umutsaydam.zenfocus.presentation.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.navigation.Route

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    var vibrateState = settingsViewModel.defaultVibrateState.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.outlineVariant,
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
                            navController.popBackStack()
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
                            navController.navigate(Route.Appearance.route)
                        }
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.change_the_app_language),
                        onClick = {
                            navController.navigate(Route.AppLanguage.route)
                        }
                    )
                    MenuItemSwitch(
                        menuTitle = stringResource(R.string.vibrate),
                        onClick = { newState ->
                            settingsViewModel.setVibrateState(newState)
                        },
                        isChecked = vibrateState.value
                    )
                }
            )
            SettingsSection(
                content = {
                    MenuItem(
                        menuTitle = stringResource(R.string.policy),
                        onClick = {
                            navController.navigate(Route.Policy.route)
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
                    MenuItem(
                        headIcon = R.drawable.ic_account,
                        menuTitle = stringResource(R.string.sign_in_sign_up),
                        onClick = {
                            navController.navigate(Route.Auth.route)
                        }
                    )
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