package com.lanars.compose_easy_route.generator.model

data class NavGraphInfo(
    val route: String,
    val simpleName: String,
    val qualifiedName: String,
    val startRoute: String,
    val destinations: List<DestinationWithParams>,
    val nestedGraphs: List<NavGraphInfo>,
    val isRoot: Boolean = false
)
