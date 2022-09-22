package com.lanars.compose_easy_route.generator.generation.template

import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.generator.model.DestinationWithParams
import com.lanars.compose_easy_route.generator.model.NavGraphInfo
import com.lanars.compose_easy_route.generator.utils.toVariableName

fun getNavGraphCode(navGraphInfo: NavGraphInfo): String {
    return """
    |NavigationGraph(
    |  route = "${navGraphInfo.route}",
    |  startRoute = "${navGraphInfo.startRoute}",
    |  destinations = listOf(${getDestinationsListCode(navGraphInfo.destinations)}),
    |  nestedGraphs = listOf(${getNestedGraphsListCode(navGraphInfo.nestedGraphs)}),
    |)
    """.trimMargin()
}

private fun getDestinationsListCode(destinations: List<DestinationWithParams>): String {
    if (destinations.isEmpty()) return String.empty
    return "\n\t\t" + destinations.joinToString(",\n\t\t") {
        it.destinationName
    } + "\n\t"
}

private fun getNestedGraphsListCode(navGraphs: List<NavGraphInfo>): String {
    if (navGraphs.isEmpty()) return String.empty
    return "\n\t\t" + navGraphs.joinToString(",\n\t\t") {
        it.route.toVariableName()
    } + "\n\t"
}
