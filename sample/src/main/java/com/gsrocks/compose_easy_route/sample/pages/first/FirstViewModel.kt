package com.gsrocks.compose_easy_route.sample.pages.first

import androidx.lifecycle.ViewModel
import com.gsrocks.compose_easy_route.SecondPageDestination
import com.gsrocks.compose_easy_route.navigation.NavigationManager
import com.gsrocks.compose_easy_route.sample.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val navigationManager: NavigationManager
) : ViewModel() {

    fun navigateToSecond(name: String) {
        navigationManager.navigate(
            SecondPageDestination(
                person = Person(
                    name = name,
                    age = 23
                ),
                number = 42
            )
        )
    }
}
