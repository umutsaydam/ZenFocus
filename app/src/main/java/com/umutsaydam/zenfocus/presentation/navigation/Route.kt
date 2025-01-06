package com.umutsaydam.zenfocus.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
    data object AccountConfirm : Route(
        route = "AccountConfirm/{email}/{shouldResend}",
        arguments = listOf(
            navArgument("email") { type = NavType.StringType },
            navArgument("shouldResend") { type = NavType.BoolType },
        )
    )
}