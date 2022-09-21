package com.lanars.compose_easy_route.navargs.privitives.array

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.encodedComma

object IntArrayNavigationType : NavigationType<IntArray?>() {

    override fun put(bundle: Bundle, key: String, value: IntArray?) {
        bundle.putIntArray(key, value)
    }

    override fun get(bundle: Bundle, key: String): IntArray? {
        return bundle.getIntArray(key)
    }

    override fun parseValue(value: String): IntArray? {
        return when (value) {
            DECODED_NULL -> null
            "[]" -> intArrayOf()
            else -> {
                val contentValue = value.subSequence(1, value.length - 1)
                val splits = if (contentValue.contains(encodedComma)) {
                    contentValue.split(encodedComma)
                } else {
                    contentValue.split(",")
                }

                IntArray(splits.size) { IntType.parseValue(splits[it]) }
            }
        }
    }

    override fun serializeValue(value: IntArray?): String {
        value ?: return ENCODED_NULL
        return "[${value.joinToString(",") { it.toString() }}]"
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): IntArray? {
        return navBackStackEntry.arguments?.getIntArray(key)
    }
}
