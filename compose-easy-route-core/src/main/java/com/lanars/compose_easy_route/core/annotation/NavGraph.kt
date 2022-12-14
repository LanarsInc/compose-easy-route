package com.lanars.compose_easy_route.core.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NavGraph(
    val route: String,
    val parent: KClass<*> = RootNavGraph::class,
    val independent: Boolean = false
)
