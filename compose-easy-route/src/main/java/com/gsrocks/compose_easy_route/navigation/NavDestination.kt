package com.gsrocks.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface NavDestination {
    val routeName: String

    val fullRoute: String

    val arguments: List<NamedNavArgument>

    @Composable
    fun Content(backStackEntry: NavBackStackEntry)
}
