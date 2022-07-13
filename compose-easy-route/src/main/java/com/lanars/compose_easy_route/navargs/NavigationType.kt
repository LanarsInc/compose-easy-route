package com.lanars.compose_easy_route.navargs

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType

abstract class NavigationType<T : Any?> : NavType<T>(isNullableAllowed = true) {

    abstract fun serializeValue(value: T): String

    abstract fun get(navBackStackEntry: NavBackStackEntry, key: String): T
}
