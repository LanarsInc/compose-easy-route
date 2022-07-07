package com.gsrocks.compose_easy_route.navigation

data class NavigationGraph(
    val route: String,
    val startRoute: String,
    val destinations: List<NavDestination>,
    val nestedGraphs: List<NavigationGraph> = emptyList()
)
