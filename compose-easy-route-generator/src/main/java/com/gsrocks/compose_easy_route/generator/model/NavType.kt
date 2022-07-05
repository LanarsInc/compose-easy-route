package com.gsrocks.compose_easy_route.generator.model

sealed class NavType(val simpleName: String, val getFunName: String) {
    object StringNavType : NavType(simpleName = "NavType.StringType", getFunName = "getString")
    object IntNavType : NavType(simpleName = "NavType.IntType", getFunName = "getInt")
    object BoolNavType : NavType(simpleName = "NavType.BoolType", getFunName = "getBool")
    object FloatNavType : NavType(simpleName = "NavType.FloatType", getFunName = "getFloat")
    object LongNavType : NavType(simpleName = "NavType.LongType", getFunName = "getLong")
    class SerializableNavType(actualType: String) :
        NavType(simpleName = "SerializableNavType<${actualType}>()", "getSerializable")

    companion object {
        fun forType(qualifiedName: String): NavType {
            return when (qualifiedName) {
                String::class.qualifiedName -> StringNavType
                Int::class.qualifiedName -> IntNavType
                Boolean::class.qualifiedName -> BoolNavType
                Float::class.qualifiedName -> FloatNavType
                LongNavType::class.qualifiedName -> LongNavType
                else -> throw IllegalArgumentException()
            }
        }
    }
}
