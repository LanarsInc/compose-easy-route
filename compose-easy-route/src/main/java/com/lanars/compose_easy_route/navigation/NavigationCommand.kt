package com.lanars.compose_easy_route.navigation

import com.lanars.compose_easy_route.core.model.NavDirection

sealed class NavigationCommand {
    object Default : NavigationCommand()

    object PopCommand : NavigationCommand()

    data class PopUpToCommand(
        val route: String,
        val inclusive: Boolean,
        val saveState: Boolean = false
    ) : NavigationCommand() {
        constructor(
            destination: NavDestination,
            inclusive: Boolean,
            saveState: Boolean = false
        ) : this(
            route = destination.fullRoute,
            inclusive = inclusive,
            saveState = saveState
        )
    }

    data class NavigateCommand(
        val direction: NavDirection,
        val navOptions: NavigationOptions? = null
    ) : NavigationCommand() {
        constructor(route: String, navOptions: NavigationOptions? = null) : this(
            object : NavDirection {
                override val route = route
            },
            navOptions = navOptions
        )
    }

    data class SetResult<T>(
        val destination: NavDestination? = null,
        val key: String,
        val value: T
    ) : NavigationCommand()
}
