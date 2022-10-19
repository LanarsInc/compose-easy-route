package com.lanars.compose_easy_route.navigation

import com.lanars.compose_easy_route.core.model.NavDirection
import com.lanars.compose_easy_route.navigation.options.NavigationOptions
import com.lanars.compose_easy_route.navigation.options.PopOptions

sealed class NavigationCommand {
    object Default : NavigationCommand()

    data class PopCommand(
        val popOptions: PopOptions? = null
    ) : NavigationCommand()

    data class PopUpToCommand(
        val route: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
        val popOptions: PopOptions? = null
    ) : NavigationCommand() {
        constructor(
            destination: NavDestination,
            inclusive: Boolean,
            saveState: Boolean = false,
            popOptions: PopOptions? = null
        ) : this(
            route = destination.fullRoute,
            inclusive = inclusive,
            saveState = saveState,
            popOptions = popOptions
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
