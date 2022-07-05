package com.gsrocks.compose_easy_route.sample.pages.first

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gsrocks.compose_easy_route.SecondPageDestination
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.sample.LocalNavigationProvider
import com.gsrocks.compose_easy_route.sample.models.Person

@Destination(name = "first-page")
@Composable
fun FirstPage() {
    val navigator = LocalNavigationProvider.current

    var name by rememberSaveable { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = name, onValueChange = { name = it })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navigator.navigate(
                        SecondPageDestination(
                            person = Person(
                                name = name,
                                age = 23
                            ),
                            number = 999
                        )
                    )
                }
            ) {
                Text(text = "Go to second page ->")
            }
        }
    }
}
