package com.gsrocks.compose_easy_route.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gsrocks.compose_easy_route.GreetingDestination
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.navigation.EasyRouteNavHost
import com.gsrocks.compose_easy_route.navigation.NavigationManager
import com.gsrocks.compose_easy_route.sample.ui.theme.ComposeEasyRouteSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeEasyRouteSampleTheme {
                /*val navigationManager = remember { NavigationManager() }
                EasyRouteNavHost(
                    navigationManager = navigationManager,
                    navGraph = ,
                    initialRoute = GreetingDestination.fullRoute
                )*/
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Destination("greeting-page")
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeEasyRouteSampleTheme {
        Greeting("Android")
    }
}