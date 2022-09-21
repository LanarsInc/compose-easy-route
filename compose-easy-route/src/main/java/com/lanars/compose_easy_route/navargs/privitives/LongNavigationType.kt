package com.lanars.compose_easy_route.navargs.privitives

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType

object LongNavigationType : NavigationType<Long?>() {

    override fun put(bundle: Bundle, key: String, value: Long?) {
        if (value == null) {
            bundle.putByte(key, 0)
        } else {
            bundle.putLong(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Long? {
        return longValue(bundle[key])
    }

    override fun parseValue(value: String): Long? {
        return if (value == DECODED_NULL) {
            null
        } else {
            LongType.parseValue(value)
        }
    }

    override fun serializeValue(value: Long?): String {
        return value?.toString() ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Long? {
        return longValue(navBackStackEntry.arguments?.get(key))
    }

    private fun longValue(valueForKey: Any?): Long? {
        return valueForKey as? Long
    }
}
