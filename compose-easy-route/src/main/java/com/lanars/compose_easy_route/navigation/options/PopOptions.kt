package com.lanars.compose_easy_route.navigation.options

import com.lanars.compose_easy_route.navigation.NavigationResult

data class PopOptions(
    val navigationResult: NavigationResult<*>? = null
) {
    /**
     * Builder for constructing new instances of PopOptions.
     */
    class Builder {
        private var navigationResult: NavigationResult<*>? = null

        fun setNavigationResult(result: NavigationResult<*>) {
            navigationResult = result
        }

        /**
         * @return a constructed PopOptions
         */
        fun build(): PopOptions {
           return PopOptions(this.navigationResult)
        }
    }
}
