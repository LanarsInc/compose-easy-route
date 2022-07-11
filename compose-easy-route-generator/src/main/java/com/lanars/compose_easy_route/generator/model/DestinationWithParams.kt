package com.lanars.compose_easy_route.generator.model

import com.lanars.compose_easy_route.generator.constants.Constants

data class DestinationWithParams(
    val composableName: String,
    val composableQualifiedName: String,
    val routeName: String,
    val parameters: List<FunctionParameter>,
    val deepLinks: List<DeepLink>,
    val navGraphNode: NavGraphNode? = null,
    val backStackEntryParamName: String? = null
) {
    val destinationName = composableName + Constants.DESTINATION_NAME_SUFFIX

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
