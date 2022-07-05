package com.gsrocks.compose_easy_route.navigation

import com.gsrocks.compose_easy_route.core.model.NavDirection

sealed class NavigationCommand {
    object Default : NavigationCommand()

    object PopCommand : NavigationCommand()

    data class NavigateCommand(val destination: NavDirection) : NavigationCommand()
}
