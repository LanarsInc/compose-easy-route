package com.lanars.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.lanars.compose_easy_route.core.model.NavDirection
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun EasyRouteNavHost(
    navigationManager: NavigationManager,
    navGraph: NavigationGraph,
    initialRoute: NavDirection
) {
    val navController = rememberNavController()

    LaunchedEffect(true) {
        navigationManager.commandFlow.receiveAsFlow().collect { command ->
            when (command) {
                is NavigationCommand.NavigateCommand -> {
                    if (command.direction.route.isNotEmpty()) {
                        val options = command.navOptions
                        if (options != null) {
                            navController.navigate(
                                route = command.direction.route,
                                navOptions = NavOptions.Builder()
                                    .setPopUpTo(
                                        options.popUpToRoute?.fullRoute,
                                        inclusive = options.isPopUpToInclusive(),
                                        saveState = options.shouldPopUpToSaveState()
                                    )
                                    .setLaunchSingleTop(options.shouldLaunchSingleTop())
                                    .setRestoreState(options.shouldRestoreState())
                                    .build()
                            )
                        } else {
                            navController.navigate(command.direction.route)
                        }
                    }
                }
                is NavigationCommand.PopCommand -> {
                    navController.popBackStack()
                }
                is NavigationCommand.PopUpToCommand -> {
                    navController.popBackStack(
                        command.route,
                        inclusive = command.inclusive,
                        saveState = command.saveState
                    )
                }
                else -> throw UnsupportedOperationException("Could not handle command $command")
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = initialRoute.route,
    ) {
        buildGraphs(navGraph, navController)
    }
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
