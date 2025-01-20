package com.umutsaydam.zenfocus.presentation.appLanguage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.policy.RadioButtonWithText
import com.umutsaydam.zenfocus.ui.theme.LightBackground
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun AppLanguageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appLanguageViewModel: AppLanguageViewModel = hiltViewModel()
) {
    val langList = appLanguageViewModel.langList
    val selectedLang by remember {appLanguageViewModel.defaultLang}.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = SurfaceContainerLow,
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
                            tint = Outline
                        )
                    }
                },
                containerColor = SurfaceContainerLow
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            items(count = langList.size, key = { it }) { index ->
                val lang = langList[index]
                RadioButtonWithText(
                    modifier = Modifier.background(LightBackground),
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