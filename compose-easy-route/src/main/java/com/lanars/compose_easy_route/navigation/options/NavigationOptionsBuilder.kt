package com.lanars.compose_easy_route.navigation.options

import com.lanars.compose_easy_route.navigation.NavDestination

/**
 * Construct a new [NavigationOptions]
 */
fun NavigationOptions(optionsBuilder: NavigationOptionsBuilder.() -> Unit): NavigationOptions =
    NavigationOptionsBuilder().apply(optionsBuilder).build()

/**
 * DSL for constructing a new [NavigationOptions]
 */
class NavigationOptionsBuilder {
    private val builder = NavigationOptions.Builder()

    /**
     * Whether this navigation action should launch as single-top (i.e., there will be at most
     * one copy of a given destination on the top of the back stack).
     *
     * This functions similarly to how [android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * works with activities.
     */
    var launchSingleTop: Boolean = false

    /**
     * Whether this navigation action should restore any state previously saved
     * by [PopUpToBuilder.saveState] or the `popUpToSaveState` attribute. If no state was
     * previously saved with the destination ID being navigated to, this has no effect.
     */
    @get:Suppress("GetterOnBuilder", "GetterSetterNames")
    @set:Suppress("SetterReturnsThis", "GetterSetterNames")
    var restoreState: Boolean = false

    /**
     * Pop up to a given destination before navigating. This pops all non-matching destinations
     * from the back stack until this destination is found.
     */
    var popUpToRoute: NavDestination? = null
        private set(value) {
            if (value != null) {
                require(value.fullRoute.isNotBlank()) { "Cannot pop up to an empty route" }
                field = value
                inclusive = false
            }
        }
    var popUntilRoot: Boolean = false
        private set
    private var inclusive = false
    private var saveState = false

    /**
     * Pop up to a given destination before navigating. This pops all non-matching destination routes
     * from the back stack until the destination with a matching route is found.
     *
     * @param destination route for the destination
     * @param popUpToBuilder builder used to construct a popUpTo operation
     */
    fun popUpTo(
        destination: NavDestination,
        popUpToBuilder: PopUpToBuilder.() -> Unit = {}
    ) {
        popUpToRoute = destination
        val builder = PopUpToBuilder().apply(popUpToBuilder)
        inclusive = builder.inclusive
        saveState = builder.saveState
    }

    fun popUntilRoot(
        popUpToBuilder: PopUpToBuilder.() -> Unit = {}
    ) {
        popUntilRoot = true
        val builder = PopUpToBuilder().apply(popUpToBuilder)
        inclusive = builder.inclusive
        saveState = builder.saveState
    }

    internal fun build() = builder.apply {
        setLaunchSingleTop(launchSingleTop)
        setRestoreState(restoreState)
        if (popUntilRoot) {
            setPopUntilRoot(inclusive, saveState)
        } else if (popUpToRoute != null) {
            setPopUpTo(popUpToRoute, inclusive, saveState)
        }
    }.build()
}

/**
 * DSL for customizing [NavigationOptionsBuilder.popUpTo] operations.
 */
class PopUpToBuilder {
    /**
     * Whether the `popUpTo` destination should be popped from the back stack.
     */
    var inclusive: Boolean = false

    /**
     * Whether the back stack and the state of all destinations between the
     * current destination and the [NavigationOptionsBuilder.popUpTo] ID should be saved for later
     * restoration via [NavigationOptionsBuilder.restoreState] or the `restoreState` attribute using
     * the same [NavigationOptionsBuilder.popUpTo] ID (note: this matching ID is true whether
     * [inclusive] is true or false).
     */
    @get:Suppress("GetterOnBuilder", "GetterSetterNames")
    @set:Suppress("SetterReturnsThis", "GetterSetterNames")
    var saveState: Boolean = false
}
