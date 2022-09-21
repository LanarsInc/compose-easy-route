package com.lanars.compose_easy_route.generator.model

import com.lanars.compose_easy_route.core.exception.UnsupportedNavArgumentType
import com.lanars.compose_easy_route.generator.constants.Constants

sealed class NavType(val simpleName: String, val qualifiedName: String) {
    object StringNavType :
        NavType(
            simpleName = "StringNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.StringNavigationType"
        )

    object IntNavType :
        NavType(
            simpleName = "IntNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.IntNavigationType"
        )

    object BoolNavType :
        NavType(
            simpleName = "BooleanNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.BooleanNavigationType"
        )

    object FloatNavType :
        NavType(
            simpleName = "FloatNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.FloatNavigationType"
        )

    object LongNavType :
        NavType(
            simpleName = "LongNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.LongNavigationType"
        )

    class SerializableNavType(actualType: String) :
        NavType(
            simpleName = "SerializableNavigationType<${actualType}>()",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.serializable.SerializableNavigationType"
        )

    class ParcelableNavType(actualType: String) :
        NavType(
            simpleName = "ParcelableNavigationType<${actualType}>($actualType::class.java)",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.parcelable.ParcelableNavigationType"
        )

    object StringArrayNavType :
        NavType(
            simpleName = "StringArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.array.StringArrayNavigationType"
        )

    object IntArrayNavType :
        NavType(
            simpleName = "IntArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.array.IntArrayNavigationType"
        )

    object BoolArrayNavType :
        NavType(
            simpleName = "BooleanArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.array.BooleanArrayNavigationType"
        )

    object FloatArrayNavType :
        NavType(
            simpleName = "FloatArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.array.FloatArrayNavigationType"
        )

    object LongArrayNavType :
        NavType(
            simpleName = "LongArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.array.LongArrayNavigationType"
        )

    class EnumNavType(actualType: String) :
        NavType(
            simpleName = "EnumNavigationType<${actualType}>($actualType::class.java)",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.navargs.privitives.EnumNavigationType"
        )

    class SerializableArrayNavType(actualType: String) :
        NavType(
            simpleName = "${actualType}ArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.${actualType}ArrayNavigationType"
        )

    class ParcelableArrayNavType(actualType: String) :
        NavType(
            simpleName = "${actualType}ArrayNavigationType",
            qualifiedName = "${Constants.BASE_PACKAGE_NAME}.${actualType}ArrayNavigationType"
        )

    companion object {
        fun forType(qualifiedName: String): NavType {
            return when (qualifiedName) {
                String::class.qualifiedName -> StringNavType
                Int::class.qualifiedName -> IntNavType
                Boolean::class.qualifiedName -> BoolNavType
                Float::class.qualifiedName -> FloatNavType
                Long::class.qualifiedName -> LongNavType
                else -> throw UnsupportedNavArgumentType()
            }
        }

        fun forArrayType(qualifiedName: String): NavType {
            return when (qualifiedName) {
                String::class.qualifiedName -> StringArrayNavType
                Int::class.qualifiedName -> IntArrayNavType
                Boolean::class.qualifiedName -> BoolArrayNavType
                Float::class.qualifiedName -> FloatArrayNavType
                Long::class.qualifiedName -> LongArrayNavType
                else -> throw UnsupportedNavArgumentType()
            }
        }
    }
}
