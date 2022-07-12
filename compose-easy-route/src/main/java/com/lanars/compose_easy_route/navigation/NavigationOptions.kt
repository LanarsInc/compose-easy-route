package com.lanars.compose_easy_route.navigation

/**
 * NavigationOptions stores special options for navigate actions
 */
class NavigationOptions internal constructor(
    private val singleTop: Boolean,
    private val restoreState: Boolean,
    /**
     * The destination to pop up to before navigating. When set, all non-matching destinations
     * should be popped from the back stack.
     * @return the destinationId to pop up to, clearing all intervening destinations
     * @see Builder.setPopUpTo
     *
     * @see isPopUpToInclusive
     * @see shouldPopUpToSaveState
     */
    private val popUpToInclusive: Boolean,
    private val popUpToSaveState: Boolean,
) {
    /**
     * Route for the destination to pop up to before navigating. When set, all non-matching
     * destinations should be popped from the back stack.
     * @return the destination route to pop up to, clearing all intervening destinations
     * @see Builder.setPopUpTo
     *
     * @see isPopUpToInclusive
     * @see shouldPopUpToSaveState
     */
    var popUpToRoute: NavDestination? = null
        private set

    /**
     * NavigationOptions stores special options for navigate actions
     */
    internal constructor(
        singleTop: Boolean,
        restoreState: Boolean,
        popUpToRoute: NavDestination?,
        popUpToInclusive: Boolean,
        popUpToSaveState: Boolean,
    ) : this(
        singleTop,
        restoreState,
        popUpToInclusive,
        popUpToSaveState,
    ) {
        this.popUpToRoute = popUpToRoute
    }

    /**
     * Whether this navigation action should launch as single-top (i.e., there will be at most
     * one copy of a given destination on the top of the back stack).
     *
     *
     * This functions similarly to how [android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * works with activities.
     */
    fun shouldLaunchSingleTop(): Boolean {
        return singleTop
    }

    /**
     * Whether this navigation action should restore any state previously saved
     * by [Builder.setPopUpTo] or the `popUpToSaveState` attribute.
     */
    fun shouldRestoreState(): Boolean {
        return restoreState
    }

    fun isPopUpToInclusive(): Boolean {
        return popUpToInclusive
    }

    fun shouldPopUpToSaveState(): Boolean {
        return popUpToSaveState
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as NavigationOptions
        return singleTop == that.singleTop &&
                restoreState == that.restoreState &&
                popUpToRoute == that.popUpToRoute &&
                popUpToInclusive == that.popUpToInclusive &&
                popUpToSaveState == that.popUpToSaveState
    }

    override fun hashCode(): Int {
        var result = if (shouldLaunchSingleTop()) 1 else 0
        result = 31 * result + if (shouldRestoreState()) 1 else 0
        result = 31 * result + popUpToRoute.hashCode()
        result = 31 * result + if (isPopUpToInclusive()) 1 else 0
        result = 31 * result + if (shouldPopUpToSaveState()) 1 else 0
        return result
    }

    /**
     * Builder for constructing new instances of NavigationOptions.
     */
    class Builder {
        private var singleTop = false
        private var restoreState = false

        private var popUpToRoute: NavDestination? = null
        private var popUpToInclusive = false
        private var popUpToSaveState = false

        /**
         * Launch a navigation target as single-top if you are making a lateral navigation
         * between instances of the same target (e.g. detail pages about similar data items)
         * that should not preserve history.
         *
         * @param singleTop true to launch as single-top
         */
        fun setLaunchSingleTop(singleTop: Boolean): Builder {
            this.singleTop = singleTop
            return this
        }

        /**
         * Whether this navigation action should restore any state previously saved
         * by [setPopUpTo] or the `popUpToSaveState` attribute. If no state was
         * previously saved with the destination ID being navigated to, this has no effect.
         */
        @SuppressWarnings("MissingGetterMatchingBuilder")
        fun setRestoreState(restoreState: Boolean): Builder {
            this.restoreState = restoreState
            return this
        }

        /**
         * Pop up to a given destination before navigating. This pops all non-matching destinations
         * from the back stack until this destination is found.
         *
         * @param route route for destination to pop up to, clearing all intervening destinations.
         * @param inclusive true to also pop the given destination from the back stack.
         * @param saveState true if the back stack and the state of all destinations between the
         * current destination and [route] should be saved for later restoration via
         * [setRestoreState] or the `restoreState` attribute using the same ID
         * as [popUpToRoute] (note: this matching ID is true whether [inclusive] is true or
         * false).
         * @return this Builder
         *=
         * @see NavigationOptions.isPopUpToInclusive
         */
        @JvmOverloads
        fun setPopUpTo(
            route: NavDestination?,
            inclusive: Boolean,
            saveState: Boolean = false
        ): Builder {
            popUpToRoute = route
            popUpToInclusive = inclusive
            popUpToSaveState = saveState
            return this
        }

        /**
         * @return a constructed NavigationOptions
         */
        fun build(): NavigationOptions {
            return if (popUpToRoute != null)
                NavigationOptions(
                    singleTop, restoreState,
                    popUpToRoute, popUpToInclusive, popUpToSaveState,
                )
            else
                NavigationOptions(
                    singleTop, restoreState,
                    popUpToInclusive, popUpToSaveState,
                )
        }
    }
}
