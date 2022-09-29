package com.lanars.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
    )
    val currentBackStackEntryFlow = internalCurrentBackStackEntryFlow.asSharedFlow()

    var currentBackStackEntry: NavBackStackEntry? = null
        internal set

    var previousBackStackEntry: NavBackStackEntry? = null
        internal set

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

    fun <T> setResult(key: String, value: T) {
        commandFlow.trySendBlocking(
            NavigationCommand.SetResult(
                key = key,
                value = value
            )
        )
    }

    fun <T> setResult(destination: NavDestination, key: String, value: T) {
        commandFlow.trySendBlocking(
            NavigationCommand.SetResult(
                destination = destination,
                key = key,
                value = value
            )
        )
    }
}

@Composable
fun rememberNavigationManager(): NavigationManager {
    return remember { NavigationManager() }
}

@Composable
fun NavigationManager.currentBackStackEntryAsState(): State<NavBackStackEntry?> {
    return currentBackStackEntryFlow.collectAsState(null)
}

suspend fun <T> NavigationManager.currentSaveStateHandleStateFlow(
    key: String,
    initialValue: T,
    collector: FlowCollector<T>
) {
    currentBackStackEntry?.savedStateHandle?.getStateFlow(
        key,
        initialValue
    )?.collect(collector)
}
