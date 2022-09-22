package com.lanars.compose_easy_route.sample.pages.second

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.lanars.compose_easy_route.BooksScreenDestination
import com.lanars.compose_easy_route.FriendsScreenDestination
import com.lanars.compose_easy_route.ProfileScreenDestination
import com.lanars.compose_easy_route.core.model.NavDirection

sealed class Screen(val direction: NavDirection, val title: String, val icon: ImageVector) {
    object Books : Screen(BooksScreenDestination(), "Books", Icons.Default.List)
    object FriendsList : Screen(FriendsScreenDestination(), "Friends", Icons.Default.Favorite)
    object Profile : Screen(ProfileScreenDestination(), "Profile", Icons.Default.Person)
}
