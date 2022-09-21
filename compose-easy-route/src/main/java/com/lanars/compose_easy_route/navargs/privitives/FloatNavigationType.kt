package com.lanars.compose_easy_route.navargs.privitives

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType

object FloatNavigationType : NavigationType<Float?>() {

    override fun put(bundle: Bundle, key: String, value: Float?) {
        if (value == null) {
            bundle.putByte(key, 0)
        } else {
            bundle.putFloat(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Float? {
        return floatValue(bundle[key])
    }

    override fun parseValue(value: String): Float? {
        return if (value == DECODED_NULL) {
            null
        } else {
            FloatType.parseValue(value)
        }
    }

    override fun serializeValue(value: Float?): String {
        return value?.toString() ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Float? {
        return floatValue(navBackStackEntry.arguments?.get(key))
    }

    private fun floatValue(valueForKey: Any?): Float? {
        return valueForKey as? Float
    }
}
