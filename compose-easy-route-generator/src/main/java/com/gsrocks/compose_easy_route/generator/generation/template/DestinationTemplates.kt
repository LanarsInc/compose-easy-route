package com.gsrocks.compose_easy_route.generator.generation.template

import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.gsrocks.compose_easy_route.generator.model.FunctionParameter
import com.gsrocks.compose_easy_route.generator.model.NavType

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

fun getDestinationWthParamsTemplate(destination: DestinationWithParams): String {
    return """
import androidx.compose.runtime.Composable
import ${Constants.BASE_PACKAGE_NAME}.navigation.NavDestination
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ${Constants.BASE_PACKAGE_NAME}.core.model.NavDirection
import ${Constants.BASE_PACKAGE_NAME}.navtype.SerializableNavType
import ${Constants.BASE_PACKAGE_NAME}.serializer.ParcelableNavTypeSerializer
import ${Constants.BASE_PACKAGE_NAME}.navtype.ParcelableNavType
import ${destination.composableQualifiedName}
${destination.parameters.joinToString(separator = "\n") { it.type.getImportString() }}

object ${destination.composableName}Destination : NavDestination {
    override val routeName = "${destination.routeName}"
    
    override val fullRoute = "${destination.getFullPath()}"
    
    override val arguments = listOf(
        ${getNavArgumentsCode(destination.parameters)}
    )
    
    operator fun invoke(
        ${getInvokeParamsCode(destination.parameters)}
    ): NavDirection {
        return object : NavDirection {
            override val route = "${destination.routeName}/${getRouteArgumentsCode(destination.parameters)}"
        }
    }
    
    @Composable
    override fun Content(backStackEntry: NavBackStackEntry) {
        val arguments = backStackEntry.arguments!!
        ${destination.composableName}(
            ${getContentArgumentsCode(destination.parameters)}
        )
    }
}
""".trimIndent()
}

fun getNavArgumentsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t") {
        "navArgument(\"${it.name}\") { type = ${it.type.navType.simpleName} },"
    }
}

fun getInvokeParamsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t") {
        "${it.name}: ${it.type.simpleName},"
    }
}

fun getRouteArgumentsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "/") {
        if (it.type.isSerializable) {
            "\${${it.type.navType.simpleName}.serializeValue(${it.name})}"
        } else if (it.type.isParcelable) {
            "\${${it.type.navType.simpleName}.serializeValue(${it.name})}"
        } else {
            "$${it.name}"
        }
    }
}

fun getContentArgumentsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t\t") {
        var arg = "${it.name} = arguments.${it.type.navType.getFunName}(\"${it.name}\")"
        when (it.type.navType) {
            is NavType.StringNavType,
            is NavType.ParcelableNavType -> arg += "!!"
            else -> {}
        }
        if (it.type.isSerializable) {
            arg += " as ${it.type.simpleName}"
        }
        arg += ","
        return@joinToString arg
    }
}
