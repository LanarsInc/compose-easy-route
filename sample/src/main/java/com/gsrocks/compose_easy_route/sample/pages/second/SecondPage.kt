package com.gsrocks.compose_easy_route.sample.pages.second

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
import com.gsrocks.compose_easy_route.FirstPageDestination
import com.gsrocks.compose_easy_route.ThirdPageDestination
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.sample.LocalNavigationProvider
import com.gsrocks.compose_easy_route.sample.models.Person

@Destination("second-page")
@Composable
fun SecondPage(
    person: Person,
    number: Int = 56,
) {
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
                text = "Hello, ${person.name}! Age: ${person.age}. N: $number",
                style = TextStyle(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navigator.navigate(ThirdPageDestination())
                }
            ) {
                Text(text = "Pop up to first")
            }
        }
    }
}
