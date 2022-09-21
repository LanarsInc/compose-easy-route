package com.lanars.compose_easy_route.navargs.privitives

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.serializer.utils.encodeForRoute

object StringNavigationType : NavigationType<String?>() {

    internal const val ENCODED_EMPTY_STRING = "%02%03"
    internal val DECODED_EMPTY_STRING: String = Uri.decode(ENCODED_EMPTY_STRING)

    private const val ENCODED_DEFAULT_VALUE_STRING_PREFIX = "%@def@"
    private val DECODED_DEFAULT_VALUE_STRING_PREFIX: String = Uri.decode(
        ENCODED_DEFAULT_VALUE_STRING_PREFIX
    )

    override fun put(bundle: Bundle, key: String, value: String?) {
        StringType.put(bundle, key, value)
    }

    override fun get(bundle: Bundle, key: String): String? {
        return StringType[bundle, key]
    }

    override fun parseValue(value: String): String? {
        if (value.startsWith(DECODED_DEFAULT_VALUE_STRING_PREFIX)) {
            return value.removePrefix(DECODED_DEFAULT_VALUE_STRING_PREFIX)
        }

        return when (value) {
            DECODED_NULL -> null
            DECODED_EMPTY_STRING -> String.empty
            else -> value
        }
    }

    override fun serializeValue(value: String?): String {
        return when {
            value == null -> ENCODED_NULL
            value.isEmpty() -> ENCODED_EMPTY_STRING
            else -> encodeForRoute(value)
        }
    }

    fun serializeValue(argName: String, value: String?): String {
        if ("{$argName}" == value) {
            return "$ENCODED_DEFAULT_VALUE_STRING_PREFIX${encodeForRoute(value)}"
        }

        return serializeValue(value)
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): String? {
        return navBackStackEntry.arguments?.getString(key)
    }
}
