package com.lanars.compose_easy_route.generator.utils

import com.lanars.compose_easy_route.core.utils.empty

fun String.toVariableName(): String {
    var result = this.trim()

    val firstLetterIndex = result.indexOfFirst { it.isLetter() }
    require(firstLetterIndex >= 0)

    result = result
        .substring(firstLetterIndex, result.length)
        .mapIndexedNotNull { index, it ->
            if (it.isLetterOrDigit()) {
                if (index > 0 && !result[index - 1].isLetterOrDigit()) return@mapIndexedNotNull it.uppercaseChar()
                return@mapIndexedNotNull it
            } else return@mapIndexedNotNull null
        }.joinToString(String.empty)

    require(result.isNotBlank())
    return result
}
