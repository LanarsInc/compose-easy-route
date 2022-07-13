package com.lanars.compose_easy_route.navargs.privitives.array

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.encodedComma

object LongArrayNavigationType : NavigationType<LongArray?>() {

    override fun put(bundle: Bundle, key: String, value: LongArray?) {
        bundle.putLongArray(key, value)
    }

    override fun get(bundle: Bundle, key: String): LongArray? {
        return bundle.getLongArray(key)
    }

    override fun parseValue(value: String): LongArray? {
        return when (value) {
            DECODED_NULL -> null
            "[]" -> longArrayOf()
            else -> {
                val contentValue = value.subSequence(1, value.length - 1)
                val splits = if (contentValue.contains(encodedComma)) {
                    contentValue.split(encodedComma)
                } else {
                    contentValue.split(",")
                }

                LongArray(splits.size) { LongType.parseValue(splits[it]) }
            }
        }
    }

    override fun serializeValue(value: LongArray?): String {
        value ?: return ENCODED_NULL
        return "[${value.joinToString(",") { it.toString() }}]"
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): LongArray? {
        return navBackStackEntry.arguments?.getLongArray(key)
    }
}
