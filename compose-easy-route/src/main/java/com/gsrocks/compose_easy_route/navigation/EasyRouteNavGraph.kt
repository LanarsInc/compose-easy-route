package com.gsrocks.compose_easy_route.navigation

data class EasyRouteNavGraph(
    val initialRoute: String,
    val destinations: List<NavDestination>
)
