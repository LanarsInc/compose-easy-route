package com.lanars.compose_easy_route.generator.generation.template.navtype

import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.model.GenericType

fun getSerializableArrayNavTypeTemplate(type: GenericType): String {
    return """
        import android.os.Bundle
        import androidx.navigation.NavBackStackEntry
        import ${Constants.BASE_PACKAGE_NAME}.navargs.NavigationType
        import ${Constants.BASE_PACKAGE_NAME}.navargs.privitives.DECODED_NULL
        import ${Constants.BASE_PACKAGE_NAME}.navargs.privitives.ENCODED_NULL
        import ${Constants.BASE_PACKAGE_NAME}.navargs.privitives.encodedComma
        import ${Constants.BASE_PACKAGE_NAME}.serializer.SerializableNavTypeSerializer
        import ${Constants.BASE_PACKAGE_NAME}.serializer.utils.encodeForRoute
        import java.io.Serializable
        import ${type.qualifiedName}
        
        @Suppress("UNCHECKED_CAST")
        object ${type.simpleName}ArrayNavigationType : NavigationType<Array<${type.simpleName}>?>() {
        
            private val serializer: SerializableNavTypeSerializer = SerializableNavTypeSerializer()

            override fun get(bundle: Bundle, key: String): Array<${type.simpleName}>? {
                return bundle.getSerializable(key) as Array<${type.simpleName}>?
            }
        
            override fun parseValue(value: String): Array<${type.simpleName}>? {
                return when (value) {
                    DECODED_NULL -> null
                    "[]" -> emptyArray<${type.simpleName}>()
                    else -> {
                        val splits = value.subSequence(1, value.length - 1).split(encodedComma)
                        Array<${type.simpleName}>(splits.size) {
                            serializer.fromRouteString(splits[it]) as ${type.simpleName}
                        }
                    }
                }
            }
        
            override fun put(bundle: Bundle, key: String, value: Array<${type.simpleName}>?) {
                bundle.putSerializable(key, value)
            }
        
            override fun serializeValue(value: Array<${type.simpleName}>?): String {
                return if (value == null) {
                    ENCODED_NULL
                } else {
                    encodeForRoute(
                        "[" + value.joinToString(encodedComma) { serializer.toRouteString(it) } + "]"
                    )
                }
            }
        
            override fun get(navBackStackEntry: NavBackStackEntry, key: String): Array<${type.simpleName}>? {
                return navBackStackEntry.arguments?.getSerializable(key) as Array<${type.simpleName}>?
            }
        }
    """.trimIndent()
}
