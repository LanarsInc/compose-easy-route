package com.lanars.compose_easy_route.navargs.privitives

import android.net.Uri
import com.lanars.compose_easy_route.serializer.utils.encodeForRoute

const val ENCODED_NULL = "null"

val DECODED_NULL: String = Uri.decode(ENCODED_NULL)

val encodedComma = encodeForRoute(",")

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <E : Enum<*>> Class<E>.valueOfIgnoreCase(enumValueName: String): E {
    return enumConstants.firstOrNull { constant ->
        constant.name.equals(enumValueName, ignoreCase = true)
    } ?: throw IllegalArgumentException(
        "Enum value $enumValueName not found for type ${this.name}."
    )
}
