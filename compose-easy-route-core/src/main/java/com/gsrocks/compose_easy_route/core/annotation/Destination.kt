package com.gsrocks.compose_easy_route.core.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Destination(
    val name: String,
    val deepLinks: Array<EasyRouteDeepLink> = []
)
