package com.lanars.compose_easy_route.navargs.parcelable

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavBackStackEntry
import com.lanars.compose_easy_route.navargs.NavigationType
import com.lanars.compose_easy_route.serializer.ParcelableNavTypeSerializer
import com.lanars.compose_easy_route.serializer.utils.encodeForRoute

@Suppress("UNCHECKED_CAST")
class ParcelableNavigationType<T : Parcelable>(
    private val jClass: Class<out Parcelable>
) : NavigationType<T>() {
    private val serializer = ParcelableNavTypeSerializer(jClass)

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        return serializer.fromRouteString(value) as T
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    override fun serializeValue(value: T): String {
        return encodeForRoute(serializer.toRouteString(value))
    }

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): T {
        return navBackStackEntry.arguments?.getParcelable<T>(key) as T
    }
}
