package com.lanars.compose_easy_route.sample.pages.third

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var text by mutableStateOf("Initial")
}
