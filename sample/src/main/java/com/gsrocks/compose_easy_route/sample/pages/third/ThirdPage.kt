package com.gsrocks.compose_easy_route.sample.pages.third

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.core.annotation.EasyRouteDeepLink

@Destination(
    name = "third-page",
    deepLinks = [
        EasyRouteDeepLink(
            uriPattern = "https://www.example.com/third/?name={name}"
        )
    ]
)
@Composable
fun ThirdPage(name: String) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello, $name!",
                style = TextStyle(fontSize = 24.sp)
            )
        }
    }
}