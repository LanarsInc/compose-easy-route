package com.gsrocks.compose_easy_route.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.gsrocks.compose_easy_route.FirstPageDestination
import com.gsrocks.compose_easy_route.RootNavGraph
import com.gsrocks.compose_easy_route.navigation.EasyRouteNavHost
import com.gsrocks.compose_easy_route.navigation.NavigationManager
import com.gsrocks.compose_easy_route.sample.ui.theme.ComposeEasyRouteSampleTheme

val LocalNavigationProvider = staticCompositionLocalOf { NavigationManager() }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeEasyRouteSampleTheme {
                val navigationManager = remember { NavigationManager() }

                CompositionLocalProvider(LocalNavigationProvider provides navigationManager) {
                    EasyRouteNavHost(
                        navigationManager = navigationManager,
                        navGraph = RootNavGraph,
                        initialRoute = FirstPageDestination.fullRoute
                    )
                }
            }
        }
    }
}
