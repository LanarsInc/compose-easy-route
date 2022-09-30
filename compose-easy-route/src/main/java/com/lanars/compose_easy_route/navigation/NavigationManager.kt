package com.lanars.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.core.model.NavDirection
import com.lanars.compose_easy_route.navigation.options.NavigationOptions
import com.lanars.compose_easy_route.navigation.options.NavigationOptionsBuilder
import com.lanars.compose_easy_route.navigation.options.PopOptions
import com.lanars.compose_easy_route.navigation.options.PopOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*

class NavigationManager {
    internal val commandFlow = Channel<NavigationCommand>(Channel.UNLIMITED)

    internal val internalCurrentBackStackEntryFlow = MutableSharedFlow<NavBackStackEntry>()
    val currentBackStackEntryFlow = internalCurrentBackStackEntryFlow.asSharedFlow()

    var currentBackStackEntry: NavBackStackEntry? = null
        internal set

    var previousBackStackEntry: NavBackStackEntry? = null
        internal set

    fun popBackStack(
        builder: PopOptionsBuilder.() -> Unit = {}
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.PopCommand(PopOptions(builder))
        )
    }

    fun popBackStack(
        destination: NavDestination,
        inclusive: Boolean,
        saveState: Boolean = false,
        builder: PopOptionsBuilder.() -> Unit = {}
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.PopUpToCommand(
                destination,
                inclusive,
                saveState,
                PopOptions(builder)
            )
        )
    }

    fun popBackStack(
        route: String,
        inclusive: Boolean,
        saveState: Boolean = false,
        builder: PopOptionsBuilder.() -> Unit = {}
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.PopUpToCommand(
                route,
                inclusive,
                saveState,
                PopOptions(builder)
            )
        )
    }

    fun navigate(
        direction: NavDirection,
        navOptions: NavigationOptions? = null
    ) {
        commandFlow.trySendBlocking(
            NavigationCommand.NavigateCommand(
                direction,
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

    fun navigate(
        route: String,
        builder: NavigationOptionsBuilder.() -> Unit
    ) {
        navigate(route, NavigationOptions(builder))
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

suspend fun <T> NavBackStackEntry.collectResult(
    key: String,
    initialValue: T,
    collector: FlowCollector<T>
) {
    savedStateHandle.getStateFlow(
        key,
        initialValue
    ).collect {
        collector.emit(it)
        savedStateHandle.remove<T>(key)
    }
}
