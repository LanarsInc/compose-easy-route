package com.lanars.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.core.model.NavDirection
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*

class NavigationManager {
    internal val commandFlow = Channel<NavigationCommand>(Channel.UNLIMITED)

    internal val internalCurrentBackStackEntryFlow = MutableSharedFlow<NavBackStackEntry>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply {
        onEach { currentBackStackEntry = it }
    }
    val currentBackStackEntryFlow = internalCurrentBackStackEntryFlow.asSharedFlow()

    var currentBackStackEntry: NavBackStackEntry? = null
        private set

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

@Composable
fun rememberNavigationManager(): NavigationManager {
    return rememberSaveable {
        NavigationManager()
    }
}

@Composable
fun NavigationManager.currentBackStackEntryAsState(): State<NavBackStackEntry?> {
    return currentBackStackEntryFlow.collectAsState(null)
}
