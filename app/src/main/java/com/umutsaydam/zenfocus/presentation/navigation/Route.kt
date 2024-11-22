package com.umutsaydam.zenfocus.presentation.navigation

import androidx.navigation.NamedNavArgument

sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Appearance : Route("Appearance")
    data object AppLanguage : Route("AppLanguage")
    data object Auth : Route("Auth")
    data object FocusMode : Route("FocusMode")
    data object Home : Route("Home")
    data object Policy : Route("Policy")
    data object Settings : Route("Settings")
}