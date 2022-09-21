package com.lanars.compose_easy_route.navargs.privitives.array

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.encodedComma

@Suppress("UNCHECKED_CAST")
class EnumArrayNavigationType<E : Enum<*>>(
    private val converter: (List<String>) -> Array<E>
) : NavigationType<Array<E>?>() {

    override fun put(bundle: Bundle, key: String, value: Array<E>?) {
        bundle.putSerializable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Array<E>? {
        return bundle.getSerializable(key) as Array<E>?
    }

    override fun parseValue(value: String): Array<E>? {
        if (value == DECODED_NULL) return null

        if (value == "[]") return converter(listOf())

        val contentValue = value.subSequence(1, value.length - 1)
        val splits = if (contentValue.contains(encodedComma)) {
            contentValue.split(encodedComma)
        } else {
            contentValue.split(",")
        }

        return converter(splits)
    }

    override fun serializeValue(value: Array<E>?): String {
        value ?: return ENCODED_NULL
        return "[${value.joinToString(",") { it.name }}]"
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Array<E>? {
        return navBackStackEntry.arguments?.getSerializable(key) as Array<E>?
    }
}
