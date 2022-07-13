package com.lanars.compose_easy_route.navargs.privitives

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType

@Suppress("UNCHECKED_CAST")
class EnumNavigationType<E : Enum<*>>(
    private val enumType: Class<E>
) : NavigationType<E?>() {

    override fun put(bundle: Bundle, key: String, value: E?) {
        bundle.putSerializable(key, value)
    }

    override fun get(bundle: Bundle, key: String): E? {
        return bundle.getSerializable(key) as E?
    }

    override fun parseValue(value: String): E? {
        if (value == DECODED_NULL) return null

        return enumType.valueOfIgnoreCase(value)
    }

    override fun serializeValue(value: E?): String {
        return value?.name ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): E? {
        return navBackStackEntry.arguments?.getSerializable(key) as E?
    }
}

