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
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
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
    val selectedLang by remember { appLanguageViewModel.defaultLang }.collectAsState()

    StatusBarSwitcher()

    Scaffold(
        modifier = modifier,
        containerColor = SurfaceContainerLow,
        topBar = {
            LanguageScreenTopBar(navController)
        }
    ) { paddingValues ->
        LanguageList(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding()
            ),
            langList = langList,
            selectedLang = selectedLang,
            onLanguageSelected = { lang ->
                appLanguageViewModel.setDefaultLang(lang)
            }
        )
    }
}

@Composable
fun LanguageScreenTopBar(navController: NavHostController) {
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

@Composable
fun LanguageList(
    modifier: Modifier,
    langList: List<String>,
    selectedLang: String,
    onLanguageSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(count = langList.size, key = { it }) { index ->
            val lang = langList[index]
            LanguageItem(
                lang = lang,
                isSelected = lang == selectedLang,
                onLanguageSelected = onLanguageSelected
            )
        }
    }
}

@Composable
fun LanguageItem(
    lang: String,
    isSelected: Boolean,
    onLanguageSelected: (String) -> Unit
) {
    RadioButtonWithText(
        modifier = Modifier.background(LightBackground),
        radioSelected = isSelected,
        radioText = lang,
        onClick = { onLanguageSelected(lang) }
    )
}