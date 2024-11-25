package com.umutsaydam.zenfocus.presentation.appLanguage

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.policy.RadioButtonWithText
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun AppLanguageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appLanguageViewModel: AppLanguageViewModel = hiltViewModel()
) {
    val langList = appLanguageViewModel.langList.value
    val selectedLang by appLanguageViewModel.defaultLang.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackOrIgnore()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
                val lang = langList[index]
                Log.d("R/T", "$lang - $selectedLang")
                RadioButtonWithText(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    radioSelected = lang == selectedLang,
                    radioText = lang,
                    onClick = {
                        appLanguageViewModel.setDefaultLang(lang)
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