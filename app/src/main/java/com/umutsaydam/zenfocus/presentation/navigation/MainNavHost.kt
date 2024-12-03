package com.umutsaydam.zenfocus.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.presentation.appLanguage.AppLanguageScreen
import com.umutsaydam.zenfocus.presentation.appearance.AppearanceScreen
import com.umutsaydam.zenfocus.presentation.auth.AccountConfirmScreen
import com.umutsaydam.zenfocus.presentation.auth.AuthScreen
import com.umutsaydam.zenfocus.presentation.focusMode.FocusModeScreen
import com.umutsaydam.zenfocus.presentation.home.HomeScreen
import com.umutsaydam.zenfocus.presentation.navTransitionManager.NavigationTransitionManager
import com.umutsaydam.zenfocus.presentation.policy.PolicyScreen
import com.umutsaydam.zenfocus.presentation.settings.SettingsScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navTransitionManager = NavigationTransitionManager()

    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        composable(
            Route.Home.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.Home.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.Home.route, fromRoute)
            }
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            Route.Settings.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.Settings.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.Settings.route, fromRoute)
            }
        ) {
            SettingsScreen(navController = navController)
        }

        composable(
            Route.Policy.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.Policy.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.Policy.route, fromRoute)
            }
        ) {
            PolicyScreen(navController = navController)
        }

        composable(
            Route.Auth.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.Auth.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.Auth.route, fromRoute)
            }
        ) {
            AuthScreen(navController = navController)
        }

        composable(
            Route.Appearance.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.Appearance.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.Appearance.route, fromRoute)
            }
        ) {
            AppearanceScreen(navController = navController)
        }

        composable(
            Route.AppLanguage.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.AppLanguage.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.AppLanguage.route, fromRoute)
            }
        ) {
            AppLanguageScreen(navController = navController)
        }

        composable(
            Route.FocusMode.route,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.FocusMode.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.FocusMode.route, fromRoute)
            }
        ) {
            FocusModeScreen(navController = navController)
        }

        composable(
            route = Route.AccountConfirm.route,
            arguments = Route.AccountConfirm.arguments,
            enterTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getEnterTransition(Route.FocusMode.route, fromRoute)
            },
            exitTransition = {
                val fromRoute = navController.previousBackStackEntry?.destination?.route
                navTransitionManager.getExitTransition(Route.FocusMode.route, fromRoute)
            }
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            Log.i("R/T", "$email")
            if (!email.isNullOrEmpty()) {
                AccountConfirmScreen(email = email, navController = navController)
            }
        }
    }
}
