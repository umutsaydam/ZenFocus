package com.umutsaydam.zenfocus.presentation.navTransitionManager

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.umutsaydam.zenfocus.presentation.navigation.Route

class NavigationTransitionManager {

    private val animationDuration = 300
    private val easing = LinearOutSlowInEasing

    fun getEnterTransition(targetRoute: String, fromRoute: String?): EnterTransition {
        return when {
            targetRoute == Route.Settings.route && fromRoute == Route.Home.route -> {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            targetRoute == Route.FocusMode.route && fromRoute == Route.Home.route -> {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            targetRoute in listOf(
                Route.Auth.route,
                Route.Policy.route,
                Route.AppLanguage.route,
                Route.Appearance.route
            ) -> {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            else -> fadeIn(
                animationSpec = tween(durationMillis = animationDuration, easing = easing)
            )
        }
    }

    fun getExitTransition(targetRoute: String, fromRoute: String?): ExitTransition {
        return when {
            fromRoute == Route.Settings.route && targetRoute == Route.Home.route -> {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            fromRoute == Route.FocusMode.route && targetRoute == Route.Home.route -> {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            fromRoute in listOf(
                Route.Auth.route,
                Route.Policy.route,
                Route.AppLanguage.route,
                Route.Appearance.route
            ) -> {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = animationDuration, easing = easing)
                )
            }

            else -> fadeOut(
                animationSpec = tween(durationMillis = animationDuration, easing = easing)
            )
        }
    }
}
