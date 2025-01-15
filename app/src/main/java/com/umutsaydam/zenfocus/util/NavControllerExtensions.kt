package com.umutsaydam.zenfocus.util

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun NavController.safeNavigate(route: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route)
    }
}

fun NavController.safeNavigateAndClearBackStack(route: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }
}

fun NavController.popBackStackOrIgnore() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
