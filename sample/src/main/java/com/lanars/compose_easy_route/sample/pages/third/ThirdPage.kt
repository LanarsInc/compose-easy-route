package com.lanars.compose_easy_route.sample.pages.third

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.FourthPageDestination
import com.lanars.compose_easy_route.SecondPageDestination
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.core.annotation.ParentBackStackEntry
import com.lanars.compose_easy_route.sample.LocalNavigationProvider
import com.lanars.compose_easy_route.sample.SettingsNavGraph

@SettingsNavGraph(start = true)
@Destination(name = "third-page")
@Composable
fun ThirdPage(
    @ParentBackStackEntry parentBackStackEntry: NavBackStackEntry
) {
    val viewModel = hiltViewModel<SharedViewModel>(parentBackStackEntry)
    val navigator = LocalNavigationProvider.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.text,
                style = TextStyle(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.text = "From third" }) {
                Text(text = "Set text")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navigator.navigate(FourthPageDestination()) {
                        popUpTo(SecondPageDestination) { inclusive = false }
                    }
                }
            ) {
                Text(text = "Pop to second and push fourth")
            }
        }
    }
}
