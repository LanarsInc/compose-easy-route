package com.gsrocks.compose_easy_route.generator.model

data class ParamType(
    val simpleName: String,
    val qualifiedName: String,
    val navType: NavType,
    val isSerializable: Boolean
) {
    fun getImportString(): String {
        return "import $qualifiedName"
    }
}
