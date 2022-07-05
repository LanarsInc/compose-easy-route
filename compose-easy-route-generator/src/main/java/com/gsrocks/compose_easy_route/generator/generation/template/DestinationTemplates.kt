package com.gsrocks.compose_easy_route.generator.generation.template

import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams

fun getDestinationNoParamsTemplate(rawDestination: DestinationWithParams): String {
    return """
import androidx.compose.runtime.Composable
import ${Constants.BASE_PACKAGE_NAME}.navigation.NavDestination
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NamedNavArgument
import ${rawDestination.composableQualifiedName}
${rawDestination.parameters.joinToString(separator = "\n") { it.type.getImportString() }}

object ${rawDestination.composableName}Destination : NavDestination {
    override val routeName = "${rawDestination.routeName}"
    
    override val fullRoute = "${rawDestination.getFullPath()}"
    
    override val arguments = emptyList<NamedNavArgument>()
    
    @Composable
    override fun Content(backStackEntry: NavBackStackEntry) {
        ${rawDestination.composableName}()
    }
}
""".trimIndent()
}

fun getDestinationWthParamsTemplate(rawDestination: DestinationWithParams): String {
    return """
import androidx.compose.runtime.Composable
import ${Constants.BASE_PACKAGE_NAME}.navigation.NavDestination
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ${Constants.BASE_PACKAGE_NAME}.core.model.NavDirection
import ${Constants.BASE_PACKAGE_NAME}.navtype.SerializableNavType
import ${rawDestination.composableQualifiedName}
${rawDestination.parameters.joinToString(separator = "\n") { it.type.getImportString() }}

object ${rawDestination.composableName}Destination : NavDestination {
    override val routeName = "${rawDestination.routeName}"
    
    override val fullRoute = "${rawDestination.getFullPath()}"
    
    override val arguments = listOf(
        ${rawDestination.parameters.joinToString(separator = "\n") {
            "navArgument(\"${it.name}\") { type = ${it.type.navType.simpleName} },"
        }}
    )
    
    operator fun invoke(
        ${
            rawDestination.parameters.joinToString(separator = "\n") {
                "${it.name}: ${it.type.simpleName},"
            }
        }
    ): NavDirection {
        return object : NavDirection {
            override val route = "${rawDestination.routeName}/${
                rawDestination.parameters.joinToString(separator = "/") {
                    if (it.type.isSerializable) {
                        "\${SerializableNavType<${it.type.simpleName}>().serializeValue(${it.name})}"
                    } else {
                        "$${it.name}"
                    }
                }
            }"
        }
    }
    
    @Composable
    override fun Content(backStackEntry: NavBackStackEntry) {
        val arguments = backStackEntry.arguments!!
        ${rawDestination.composableName}(
            ${
                rawDestination.parameters.joinToString(separator = "\n") {
                    "${it.name} = arguments.${it.type.navType.getFunName}(\"${it.name}\")!! as ${it.type.simpleName},"
                }
            }
        )
    }
}
""".trimIndent()
}