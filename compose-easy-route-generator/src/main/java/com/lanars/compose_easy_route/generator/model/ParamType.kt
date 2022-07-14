package com.lanars.compose_easy_route.generator.model

data class ParamType(
    val simpleName: String,
    val qualifiedName: String,
    val navType: NavType,
    val isSerializable: Boolean,
    val isParcelable: Boolean,
    val genericType: GenericType? = null,
    val isNullable: Boolean,
    val isEnum: Boolean
) {
    fun getTypeName(): String {
        var name = simpleName
        if (genericType != null) {
            name += "<${genericType.simpleName}"
            if (genericType.isNullable) {
                name += "?"
            }
            name += ">"
        }
        if (isNullable) {
            name += "?"
        }
        return name
    }
}
