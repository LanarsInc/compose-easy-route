package com.gsrocks.compose_easy_route.sample.pages.first

import androidx.compose.runtime.Composable
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.sample.models.Person

@Destination(name = "first-page")
@Composable
fun FirstPage(
    person: Person,
    name: String,
    age: Int
) {

}