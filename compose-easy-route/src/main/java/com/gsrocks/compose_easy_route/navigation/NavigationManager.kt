package com.gsrocks.compose_easy_route.navigation

import com.gsrocks.compose_easy_route.core.model.NavDirection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking

class NavigationManager {
    val commandFlow = Channel<NavigationCommand>(Channel.UNLIMITED)

    fun popBackStack() {
        commandFlow.trySendBlocking(NavigationCommand.PopCommand)
    }

    fun popBackStack(
        destination: NavDestination,
        inclusive: Boolean,
        saveState: Boolean = false
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.PopUpToCommand(
                destination,
                inclusive,
                saveState
            )
        )
    }

    fun popBackStack(
        route: String,
        inclusive: Boolean,
        saveState: Boolean = false
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.PopUpToCommand(
                route,
                inclusive,
                saveState
            )
        )
    }

    fun navigate(
        destination: NavDirection,
        navOptions: NavigationOptions? = null
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.NavigateCommand(
                destination,
                navOptions
            )
        )
    }

    fun navigate(
        direction: NavDirection,
        builder: NavigationOptionsBuilder.() -> Unit
    ) {
        navigate(
            direction,
            NavigationOptions(builder)
        )
    }

    fun navigate(
        route: String,
        navOptions: NavigationOptions? = null
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.NavigateCommand(
                route,
                navOptions
            )
        )
    }

    fun navigate(route: String, builder: NavigationOptionsBuilder.() -> Unit) {
        navigate(route, NavigationOptions(builder))
    }
}
