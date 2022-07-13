package com.lanars.compose_easy_route.navargs.privitives.array

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.StringNavigationType
import com.lanars.compose_easy_route.navargs.privitives.encodedComma
import com.lanars.compose_easy_route.serializer.utils.encodeForRoute

object StringArrayNavigationType : NavigationType<Array<String>?>() {

    override fun put(bundle: Bundle, key: String, value: Array<String>?) {
        bundle.putStringArray(key, value)
    }

    override fun get(bundle: Bundle, key: String): Array<String>? {
        return bundle.getStringArray(key)
    }

    override fun parseValue(value: String): Array<String>? {
        return when (value) {
            DECODED_NULL -> null
            "[]" -> arrayOf()
            else -> value
                .subSequence(1, value.length - 1)
                .split(encodedComma).let { splits ->
                    Array(splits.size) {
                        when (val split = splits[it]) {
                            StringNavigationType.DECODED_EMPTY_STRING -> String.empty
                            else -> split
                        }
                    }
                }
        }
    }

    override fun serializeValue(value: Array<String>?): String {
        return when (value) {
            null -> ENCODED_NULL
            else -> encodeForRoute(
                "[" + value.joinToString(encodedComma) {
                    it.ifEmpty { StringNavigationType.ENCODED_EMPTY_STRING }
                } + "]"
            )
        }
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Array<String>? {
        return navBackStackEntry.arguments?.getStringArray(key)
    }
}
