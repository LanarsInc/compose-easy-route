package com.lanars.compose_easy_route.navargs.privitives

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType

object BooleanNavigationType : NavigationType<Boolean?>() {

    override fun put(bundle: Bundle, key: String, value: Boolean?) {
        if (value == null) {
            bundle.putByte(key, 0)
        } else {
            bundle.putBoolean(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Boolean? {
        return booleanValue(bundle[key])
    }

    override fun parseValue(value: String): Boolean? {
        return if (value == DECODED_NULL) {
            null
        } else {
            BoolType.parseValue(value)
        }
    }

    override fun serializeValue(value: Boolean?): String {
        return value?.toString() ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): Boolean? {
        return booleanValue(navBackStackEntry.arguments?.get(key))
    }

    private fun booleanValue(valueForKey: Any?): Boolean? {
        return valueForKey as? Boolean
    }
}
