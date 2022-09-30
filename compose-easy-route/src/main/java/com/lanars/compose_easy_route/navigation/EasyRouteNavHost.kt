package com.lanars.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.lanars.compose_easy_route.core.model.NavDirection
import com.lanars.compose_easy_route.navigation.options.NavigationOptions
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun EasyRouteNavHost(
    navigationManager: NavigationManager,
    navGraph: NavigationGraph,
    startDirection: NavDirection,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    LaunchedEffect(true) {
        launch {
            navController.currentBackStackEntryFlow.collect {
                navigationManager.apply {
                    internalCurrentBackStackEntryFlow.emit(it)
                    currentBackStackEntry = it
                    previousBackStackEntry = navController.previousBackStackEntry
                }
            }
        }
        launch {
            navigationManager.commandFlow.receiveAsFlow().collect { command ->
                when (command) {
                    is NavigationCommand.NavigateCommand -> {
                        if (command.direction.route.isNotEmpty()) {
                            val options = command.navOptions
                            if (options != null) {
                                navController.navigate(
                                    route = command.direction.route,
                                    navOptions = getNavOptions(options, navController)
                                )
                            } else {
                                navController.navigate(command.direction.route)
                            }
                        }
                    }
                    is NavigationCommand.PopCommand -> {
                        with(navController) {
                            command.popOptions?.navigationResult?.let {
                                previousBackStackEntry?.savedStateHandle?.set(it.key, it.value)
                            }
                            popBackStack()
                        }
                    }
                    is NavigationCommand.PopUpToCommand -> {
                        with(navController) {
                            command.popOptions?.navigationResult?.let {
                                getBackStackEntry(command.route).savedStateHandle[it.key] = it.value
                            }
                            navController.popBackStack(
                                command.route,
                                inclusive = command.inclusive,
                                saveState = command.saveState
                            )
                        }
                    }
                    is NavigationCommand.SetResult<*> -> {
                        val backStackEntry = if (command.destination == null) {
                            navController.previousBackStackEntry
                        } else {
                            navController.getBackStackEntry(command.destination.fullRoute)
                        }

                        backStackEntry?.savedStateHandle?.set(command.key, command.value)
                    }
                    else -> throw UnsupportedOperationException("Could not handle command $command")
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDirection.route,
        modifier = modifier
    ) {
        buildGraphs(navGraph, navController)
    }
}

private fun getNavOptions(
    options: NavigationOptions,
    navController: NavController
): NavOptions {
    val builder = NavOptions.Builder()
    if (options.popUntilRoot) {
        builder.setPopUpTo(
            navController.graph.findStartDestination().id,
            inclusive = options.isPopUpToInclusive(),
            saveState = options.shouldPopUpToSaveState()
        )
    } else {
        builder.setPopUpTo(
            options.popUpToRoute?.fullRoute,
            inclusive = options.isPopUpToInclusive(),
            saveState = options.shouldPopUpToSaveState()
        )
    }
    return builder.setLaunchSingleTop(options.shouldLaunchSingleTop())
        .setRestoreState(options.shouldRestoreState())
        .build()
}

fun NavGraphBuilder.buildGraphs(
    navGraph: NavigationGraph,
    navController: NavController
) {
    navGraph.destinations.forEach { destination ->
        composable(
            route = destination.fullRoute,
            arguments = destination.arguments,
            deepLinks = destination.deepLinks
        ) { backStackEntry ->
            val parentId = backStackEntry.destination.parent!!.id
            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(parentId)
            }
            destination.Content(backStackEntry, parentBackStackEntry)
        }
    }

    navGraph.nestedGraphs.forEach { nestedGraph ->
        navigation(
            startDestination = nestedGraph.startRoute,
            route = nestedGraph.route
        ) {
            buildGraphs(nestedGraph, navController)
        }
    }
}
