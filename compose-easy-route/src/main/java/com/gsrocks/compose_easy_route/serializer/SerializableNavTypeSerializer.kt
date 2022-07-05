package com.gsrocks.compose_easy_route.serializer

import com.gsrocks.compose_easy_route.serializer.utils.base64ToByteArray
import com.gsrocks.compose_easy_route.serializer.utils.toBase64Str
import java.io.*

class SerializableNavTypeSerializer {

    fun toRouteString(value: Serializable): String {
        return value.toBase64()
    }

    fun fromRouteString(routeStr: String): Serializable {
        return base64ToSerializable(routeStr)
    }

    private fun Serializable.toBase64(): String {
        return ByteArrayOutputStream().use {
            val out = ObjectOutputStream(it)
            out.writeObject(this)
            out.flush()
            it.toByteArray().toBase64Str()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> base64ToSerializable(base64: String): T {
        val bytes = base64.base64ToByteArray()
        return ObjectInputStream(ByteArrayInputStream(bytes)).use {
            it.readObject() as T
        }
    }
}