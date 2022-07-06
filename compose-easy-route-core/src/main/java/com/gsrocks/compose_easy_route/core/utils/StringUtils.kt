package com.gsrocks.compose_easy_route.core.utils

val String.Companion.empty: String
    get() = ""

fun String.removeFromTo(from: String, to: String): String {
    val startIndex = indexOf(from)
    val endIndex = indexOf(to) + to.length

    return kotlin.runCatching { removeRange(startIndex, endIndex) }.getOrNull() ?: this
}
