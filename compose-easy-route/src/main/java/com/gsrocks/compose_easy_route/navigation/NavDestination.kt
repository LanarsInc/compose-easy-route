package com.gsrocks.compose_easy_route.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink

interface NavDestination {
    val routeName: String

    val fullRoute: String

    val arguments: List<NamedNavArgument>

    val deepLinks: List<NavDeepLink>

    @Composable
    fun Content(
        backStackEntry: NavBackStackEntry,
        parentBackStackEntry: NavBackStackEntry
    )
}
