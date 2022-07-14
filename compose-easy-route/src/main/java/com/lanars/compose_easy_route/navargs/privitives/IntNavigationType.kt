package com.lanars.compose_easy_route.navargs.privitives

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType

object IntNavigationType : NavigationType<Int?>() {

    override fun get(bundle: Bundle, key: String): Int? {
        return intValue(bundle[key])
    }

    override fun parseValue(value: String): Int? {
        return when (value) {
            ENCODED_NULL -> null
            else -> IntType.parseValue(value)
        }
    }

    override fun put(bundle: Bundle, key: String, value: Int?) {
        when (value) {
            null -> bundle.putByte(key, 0)
            else -> bundle.putInt(key, value)
        }
    }

    override fun serializeValue(value: Int?): String {
        return value?.toString() ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Int? {
        return intValue(navBackStackEntry.arguments?.get(key))
    }

    private fun intValue(valueFroKey: Any?): Int? {
        return valueFroKey as? Int
    }
}
