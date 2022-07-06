package com.gsrocks.compose_easy_route.navigation

import com.gsrocks.compose_easy_route.core.model.NavDirection

sealed class NavigationCommand {
    object Default : NavigationCommand()

    object PopCommand : NavigationCommand()

    data class PopUpToCommand(
        val route: String, val inclusive: Boolean
    ) : NavigationCommand() {
        constructor(destination: NavDestination, inclusive: Boolean) : this(
            route = destination.fullRoute,
            inclusive = inclusive
        )
    }

    data class NavigateCommand(val direction: NavDirection) : NavigationCommand() {
        constructor(route: String) : this(
            object : NavDirection {
                override val route = route
            }
        )
    }
}
