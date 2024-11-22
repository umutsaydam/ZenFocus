package com.umutsaydam.zenfocus.presentation.appLanguage

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.policy.RadioButtonWithText

@Composable
fun AppLanguageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val langList = listOf(
        "Türkçe",
        "İngilizce"
    )
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.outlineVariant,
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(
                    vertical = paddingValues.calculateTopPadding()
                )
        ) {
            items(count = langList.size) { index ->
                RadioButtonWithText(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    radioSelected = false,
                    radioText = langList[index],
                    onClick = {
                        //:TODO perform onClick
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AppLanguageScreenPreview(modifier: Modifier = Modifier) {
    AppLanguageScreen(navController = rememberNavController())
}