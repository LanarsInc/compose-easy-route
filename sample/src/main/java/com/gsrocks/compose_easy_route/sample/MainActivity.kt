package com.gsrocks.compose_easy_route.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.gsrocks.compose_easy_route.FirstPageDestination
import com.gsrocks.compose_easy_route.NavGraphs
import com.gsrocks.compose_easy_route.core.annotation.NavGraph
import com.gsrocks.compose_easy_route.navigation.EasyRouteNavHost
import com.gsrocks.compose_easy_route.navigation.NavigationManager
import com.gsrocks.compose_easy_route.sample.ui.theme.ComposeEasyRouteSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val LocalNavigationProvider = staticCompositionLocalOf { NavigationManager() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeEasyRouteSampleTheme {
                CompositionLocalProvider(LocalNavigationProvider provides navigationManager) {
                    EasyRouteNavHost(
                        navigationManager = navigationManager,
                        navGraph = NavGraphs.root,
                        initialRoute = FirstPageDestination()
                    )
                }
            }
        }
    }
}

@NavGraph(route = "settings", startRoute = "third-page")
annotation class SettingsNavGraph()

@NavGraph(route = "login", startRoute = "name-page", parent = SettingsNavGraph::class)
annotation class LoginNavGraph()
