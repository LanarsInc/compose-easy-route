package com.gsrocks.compose_easy_route.generator.model

data class NavGraphNode(
    val simpleName: String,
    val qualifiedName: String,
    val route: String,
    val startRoute: String,
    val parentQualifiedName: String,
    val isRoot: Boolean = false
)
