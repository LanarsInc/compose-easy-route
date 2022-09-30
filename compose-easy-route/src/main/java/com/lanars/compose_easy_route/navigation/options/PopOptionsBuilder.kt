package com.lanars.compose_easy_route.navigation.options

import com.lanars.compose_easy_route.navigation.NavigationResult

/**
 * Construct a new [PopOptions]
 */
fun PopOptions(optionsBuilder: PopOptionsBuilder.() -> Unit): PopOptions =
    PopOptionsBuilder().apply(optionsBuilder).build()

class PopOptionsBuilder {
    private val builder = PopOptions.Builder()

    var navigationResult: NavigationResult<*>? = null
        private set

    fun <T> withResult(
        key: String,
        value: T
    ) {
        navigationResult = NavigationResult(key, value)
    }

    internal fun build() = builder.apply {
        navigationResult?.let { setNavigationResult(it) }
    }.build()
}
