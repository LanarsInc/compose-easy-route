package com.gsrocks.compose_easy_route.core.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NavGraph(val route: String, val startRoute: String)
