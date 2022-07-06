package com.gsrocks.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        navGraph.destinations.forEach { destination ->
            composable(
                route = destination.fullRoute,
                arguments = destination.arguments,
            ) { backStackEntry ->
                destination.Content(backStackEntry)
            }
        }
    }
}
