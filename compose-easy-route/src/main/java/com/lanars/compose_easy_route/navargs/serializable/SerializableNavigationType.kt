package com.lanars.compose_easy_route.navargs.serializable

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.navargs.privitives.DECODED_NULL
import com.lanars.compose_easy_route.navargs.privitives.ENCODED_NULL
import com.lanars.compose_easy_route.serializer.SerializableNavTypeSerializer
import com.lanars.compose_easy_route.serializer.utils.encodeForRoute
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class SerializableNavigationType<T : Serializable> : NavigationType<T?>() {
    private val serializer: SerializableNavTypeSerializer = SerializableNavTypeSerializer()

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getSerializable(key) as T?
    }

    override fun parseValue(value: String): T? {
        return if (value == DECODED_NULL) {
            null
        } else {
            serializer.fromRouteString(value) as T?
        }
    }

    override fun put(bundle: Bundle, key: String, value: T?) {
        bundle.putSerializable(key, value)
    }

    override fun serializeValue(value: T?): String {
        return value?.let {
            encodeForRoute(serializer.toRouteString(it))
        } ?: ENCODED_NULL
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): T? {
        return navBackStackEntry.arguments?.getSerializable(key) as T?
    }
}
