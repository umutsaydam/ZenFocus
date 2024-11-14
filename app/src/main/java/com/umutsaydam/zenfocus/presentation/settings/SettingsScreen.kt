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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.outlineVariant,
        topBar = {
            IconWithTopAppBar(
                title = stringResource(R.string.settings),
                icon = R.drawable.ic_arrow_back,
                contentDescription = stringResource(R.string.back_to_home)
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
                            //TODO: perform onClick
                        }
                    )
                    MenuItem(
                        menuTitle = stringResource(R.string.change_the_app_language),
                        onClick = {
                            //TODO: perform onClick
                        }
                    )
                    MenuItemSwitch(
                        menuTitle = stringResource(R.string.vibrate),
                        onClick = {
                            //TODO: perform onClick
                        },
                    )
                }
            )
            SettingsSection(
                content = {
                    MenuItem(
                        menuTitle = stringResource(R.string.policy),
                        onClick = {
                            //TODO: perform onClick
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
                            //TODO: perform onClick
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
    SettingsScreen()
}