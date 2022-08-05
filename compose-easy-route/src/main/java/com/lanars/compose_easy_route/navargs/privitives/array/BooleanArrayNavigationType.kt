package com.lanars.compose_easy_route.navargs.privitives.array

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.encodedComma

object BooleanArrayNavigationType : NavigationType<BooleanArray?>() {

    override fun get(bundle: Bundle, key: String): BooleanArray? {
        return bundle.getBooleanArray(key)
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): BooleanArray? {
        return navBackStackEntry.arguments?.getBooleanArray(key)
    }

    override fun parseValue(value: String): BooleanArray? {
        return when (value) {
            DECODED_NULL -> null
            "[]" -> booleanArrayOf()
            else -> {
                val contentValue = value.subSequence(1, value.length - 1)
                val splits = if (contentValue.contains(encodedComma)) {
                    contentValue.split(encodedComma)
                } else {
                    contentValue.split(",")
                }

                BooleanArray(splits.size) { BoolType.parseValue(splits[it]) }
            }
        }
    }

    override fun put(bundle: Bundle, key: String, value: BooleanArray?) {
        bundle.putBooleanArray(key, value)
    }

    override fun serializeValue(value: BooleanArray?): String {
        value ?: return ENCODED_NULL
        return "[${value.joinToString(",") { it.toString() }}]"
    }
}

