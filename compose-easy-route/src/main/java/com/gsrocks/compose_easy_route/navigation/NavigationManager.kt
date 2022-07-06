package com.gsrocks.compose_easy_route.navigation

import com.gsrocks.compose_easy_route.core.model.NavDirection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking

class NavigationManager {
    val commandFlow = Channel<NavigationCommand>(Channel.UNLIMITED)

    fun popBackStack() {
        commandFlow.trySendBlocking(NavigationCommand.PopCommand)
    }

    fun popUpTo(destination: NavDestination, inclusive: Boolean) {
        commandFlow.trySendBlocking(NavigationCommand.PopUpToCommand(destination, inclusive))
    }

    fun popUpTo(route: String, inclusive: Boolean) {
        commandFlow.trySendBlocking(NavigationCommand.PopUpToCommand(route, inclusive))
    }

    fun navigate(destination: NavDirection) {
        commandFlow.trySendBlocking(NavigationCommand.NavigateCommand(destination))
    }

    fun navigate(route: String) {
        commandFlow.trySendBlocking(NavigationCommand.NavigateCommand(route))
    }
}
