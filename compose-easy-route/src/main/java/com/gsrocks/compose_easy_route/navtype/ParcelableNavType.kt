package com.gsrocks.compose_easy_route.navtype

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.gsrocks.compose_easy_route.serializer.ParcelableNavTypeSerializer
import com.gsrocks.compose_easy_route.serializer.utils.encodeForRoute

@Suppress("UNCHECKED_CAST")
class ParcelableNavType<T : Parcelable>(
    private val serializer: ParcelableNavTypeSerializer
) : NavType<T>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        return serializer.fromRouteString(value) as T
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    fun serializeValue(value: T): String {
        return encodeForRoute(serializer.toRouteString(value))
    }
}
