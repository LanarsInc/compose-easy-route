package com.gsrocks.compose_easy_route.generator.model

data class NavGraphInfo(
    val destinations: List<DestinationWithParams>,
    val nestedGraphs: List<NestedGraph>,
    val startRoute: String
)
