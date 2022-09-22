package com.lanars.compose_easy_route.sample.pages.second

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lanars.compose_easy_route.BooksScreenDestination
import com.lanars.compose_easy_route.NavGraphs
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.core.annotation.NavGraph
import com.lanars.compose_easy_route.navigation.EasyRouteNavHost
import com.lanars.compose_easy_route.navigation.NavigationManager
import com.lanars.compose_easy_route.navigation.currentBackStackEntryAsState
import com.lanars.compose_easy_route.sample.models.Person

@Destination("second-page")
@Composable
fun SecondPage(
    number: Int? = null,
    person: Person,
    strings: FloatArray,
    people: Array<Person>
) {
    val items = listOf(
        Screen.Books,
        Screen.Profile,
        Screen.FriendsList,
    )

    val navigationManager = remember { NavigationManager() }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navigationManager.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == screen.direction.route
                    } == true
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = screen.icon.name) },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navigationManager.navigate(screen.direction) {
                                popUntilRoot {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        EasyRouteNavHost(
            navigationManager = navigationManager,
            navGraph = NavGraphs.bottom,
            startDirection = BooksScreenDestination()
        )
    }
}

@NavGraph(route = "bottom")
annotation class BottomNavigationNavGraph(
    val start: Boolean = false
)
