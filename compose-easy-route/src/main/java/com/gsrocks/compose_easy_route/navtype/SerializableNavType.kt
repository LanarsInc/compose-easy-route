package com.gsrocks.compose_easy_route.navtype

import android.os.Bundle
import androidx.navigation.NavType
import com.gsrocks.compose_easy_route.serializer.SerializableNavTypeSerializer
import com.gsrocks.compose_easy_route.serializer.utils.encodeForRoute
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class SerializableNavType<T : Serializable> : NavType<T>(isNullableAllowed = false) {
    private val serializer: SerializableNavTypeSerializer = SerializableNavTypeSerializer()

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getSerializable(key) as T?
    }

    override fun parseValue(value: String): T {
        return serializer.fromRouteString(value) as T
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putSerializable(key, value)
    }

    fun serializeValue(value: T): String {
        return encodeForRoute(serializer.toRouteString(value))
    }
}