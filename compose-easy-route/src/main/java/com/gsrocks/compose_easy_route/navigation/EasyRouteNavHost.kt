package com.gsrocks.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun EasyRouteNavHost(
    navigationManager: NavigationManager,
    navGraph: NavigationGraph,
    initialRoute: String
) {
    val navController = rememberNavController()

    LaunchedEffect(true) {
        navigationManager.commandFlow.receiveAsFlow().collect { command ->
            when (command) {
                is NavigationCommand.NavigateCommand -> {
                    if (command.direction.route.isNotEmpty()) {
                        navController.navigate(
                            command.direction.route
                        )
                    }
                }
                is NavigationCommand.PopCommand -> {
                    navController.popBackStack()
                }
                is NavigationCommand.PopUpToCommand -> {
                    navController.popBackStack(
                        command.route,
                        inclusive = command.inclusive
                    )
                }
                else -> {}
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = initialRoute,
    ) {
        buildGraphs(navGraph)
    }
}

fun NavGraphBuilder.buildGraphs(navGraph: NavigationGraph) {
    navGraph.destinations.forEach { destination ->
        composable(
            route = destination.fullRoute,
            arguments = destination.arguments,
            deepLinks = destination.deepLinks
        ) { backStackEntry ->
            destination.Content(backStackEntry)
        }
    }

    navGraph.nestedGraphs.forEach { nestedGraph ->
        navigation(
            startDestination = nestedGraph.startRoute,
            route = nestedGraph.route
        ) {
            buildGraphs(nestedGraph)
        }
    }
}
