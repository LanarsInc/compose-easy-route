package com.gsrocks.compose_easy_route.core.annotation

@Retention(AnnotationRetention.SOURCE)
annotation class EasyRouteDeepLink(
    /**
     * The uri pattern from the NavDeepLink.
     */
    val uriPattern: String = "",
    /**
     * The action from the NavDeepLink.
     */
    val action: String = "",
    /**
     * The mimeType from the NavDeepLink.
     */
    val mimeType: String = ""
)
