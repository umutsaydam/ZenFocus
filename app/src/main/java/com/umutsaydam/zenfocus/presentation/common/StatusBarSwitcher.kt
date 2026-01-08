package com.umutsaydam.zenfocus.presentation.common

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun StatusBarSwitcher(
    enableDarkBg: Boolean = true
) {
    LocalActivity.current?.let {
        val window = it.window
        val view = LocalView.current
        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        windowInsetsController.isAppearanceLightNavigationBars = enableDarkBg
        windowInsetsController.isAppearanceLightStatusBars = enableDarkBg
    }
}