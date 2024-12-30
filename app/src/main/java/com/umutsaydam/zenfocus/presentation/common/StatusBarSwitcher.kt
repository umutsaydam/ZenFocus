package com.umutsaydam.zenfocus.presentation.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun StatusBarSwitcher(
    enableDarkBg: Boolean = true
) {
    val window = (LocalContext.current as Activity).window
    val view = LocalView.current
    val windowInsetsController = WindowCompat.getInsetsController(window, view)
    windowInsetsController.isAppearanceLightNavigationBars = enableDarkBg
    windowInsetsController.isAppearanceLightStatusBars = enableDarkBg
}