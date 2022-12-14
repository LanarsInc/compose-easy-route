package com.lanars.compose_easy_route.sample.pages.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.sample.LoginNavGraph

@LoginNavGraph(start = true)
@Destination("login-page")
@Composable
fun LoginPage() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = TextStyle(fontSize = 24.sp)
            )
        }
    }
}
