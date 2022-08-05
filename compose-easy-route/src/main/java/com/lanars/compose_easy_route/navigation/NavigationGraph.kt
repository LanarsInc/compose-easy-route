package com.lanars.compose_easy_route.navigation

class NavigationGraph(
    internal val route: String,
    internal val startRoute: String,
    internal val destinations: List<NavDestination>,
    internal val nestedGraphs: List<NavigationGraph> = emptyList()
)
