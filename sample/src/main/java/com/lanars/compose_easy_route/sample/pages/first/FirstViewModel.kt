package com.lanars.compose_easy_route.sample.pages.first

import androidx.lifecycle.ViewModel
import com.lanars.compose_easy_route.SecondPageDestination
import com.lanars.compose_easy_route.navigation.NavigationManager
import com.lanars.compose_easy_route.sample.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val navigationManager: NavigationManager
) : ViewModel() {

    fun navigateToSecond(name: String) {
        navigationManager.navigate(
            SecondPageDestination(
                number = 42,
                person = Person(name = name, age = 45),
                strings = floatArrayOf(4f, 4f, 866f)
            )
        )
    }
}
