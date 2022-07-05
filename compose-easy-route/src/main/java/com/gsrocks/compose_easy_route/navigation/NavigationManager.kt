package com.gsrocks.compose_easy_route.navigation

import com.gsrocks.compose_easy_route.core.model.NavDirection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking

class NavigationManager {
    val commandFlow = Channel<NavigationCommand>(Channel.UNLIMITED)

    fun popBackStack() {
        commandFlow.trySendBlocking(NavigationCommand.PopCommand)
    }

    fun popUpTo(destination: NavDirection) {}

    fun navigate(destination: NavDirection) {
        commandFlow.trySendBlocking(NavigationCommand.NavigateCommand(destination))
    }
}
