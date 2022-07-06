package com.gsrocks.compose_easy_route.generator.model

data class DestinationWithParams(
    val composableName: String,
    val composableQualifiedName: String,
    val routeName: String,
    val parameters: List<FunctionParameter>,
    val deepLinks: List<DeepLink>
) {
    fun getFullPath(): String {
        var fullPath = routeName
        parameters.forEach { funParameter ->
            fullPath += if (funParameter.hasDefault) {
                "?${funParameter.name}={${funParameter.name}}"
            } else {
                "/{${funParameter.name}}"
            }
        }
        return fullPath
    }
}
