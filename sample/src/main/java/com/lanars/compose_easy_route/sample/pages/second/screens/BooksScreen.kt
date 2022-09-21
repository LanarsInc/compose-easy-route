package com.lanars.compose_easy_route.sample.pages.second.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.sample.pages.second.BottomNavigationNavGraph

@BottomNavigationNavGraph(start = true)
@Destination("books")
@Composable
fun BooksScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Books screen", fontSize = 32.sp)
    }
}
