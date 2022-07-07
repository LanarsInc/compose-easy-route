package com.gsrocks.compose_easy_route.generator.generation.template

import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.model.DeepLink
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.gsrocks.compose_easy_route.generator.model.FunctionParameter
import com.gsrocks.compose_easy_route.generator.model.NavType

fun getDestinationNoParamsTemplate(destination: DestinationWithParams): String {
    return """
import androidx.compose.runtime.Composable
import ${Constants.BASE_PACKAGE_NAME}.navigation.NavDestination
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NamedNavArgument
import androidx.navigation.navDeepLink
import ${destination.composableQualifiedName}
import androidx.navigation.*
import ${Constants.BASE_PACKAGE_NAME}.core.model.NavDirection
${destination.parameters.joinToString(separator = "\n") { it.type.getImportString() }}

object ${destination.composableName}Destination : NavDestination {
    override val routeName = "${destination.routeName}"
    
    override val fullRoute = "${destination.getFullPath()}"
    
    override val arguments = emptyList<NamedNavArgument>()
    
    override val deepLinks = ${getDeepLinksCode(destination.deepLinks)}
    
    operator fun invoke(): NavDirection {
        return object : NavDirection {
            override val route = "${destination.routeName}"
        }
    }
    
    @Composable
    override fun Content(backStackEntry: NavBackStackEntry) {
        ${destination.composableName}()
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
import androidx.navigation.navDeepLink
import ${destination.composableQualifiedName}
import androidx.navigation.*
${destination.parameters.joinToString(separator = "\n") { it.type.getImportString() }}
${getDefaultParametersImports(destination.parameters)}

object ${destination.composableName}Destination : NavDestination {
    override val routeName = "${destination.routeName}"
    
    override val fullRoute = "${destination.getFullPath()}"
    
    override val arguments = listOf(
        ${getNavArgumentsCode(destination.parameters)}
    )
    
    override val deepLinks = ${getDeepLinksCode(destination.deepLinks)}
    
    operator fun invoke(
        ${getInvokeParamsCode(destination.parameters)}
    ): NavDirection {
        return object : NavDirection {
            override val route = "${destination.routeName}${getRouteArgumentsCode(destination.parameters)}"
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
        var code = "navArgument(\"${it.name}\") { type = ${it.type.navType.simpleName}"
        if (it.hasDefault && it.defaultValue != null) {
            code += "; defaultValue = ${it.defaultValue.code}"
        }
        code += " },"
        code
    }
}

fun getInvokeParamsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t") {
        if (it.hasDefault && it.defaultValue != null) {
            "${it.name}: ${it.type.simpleName} = ${it.defaultValue.code},"
        } else {
            "${it.name}: ${it.type.simpleName},"
        }
    }
}

fun getRouteArgumentsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "") {
        var argumentRoutePart =
            if (it.hasDefault && it.defaultValue != null) "?${it.name}=" else "/"
        argumentRoutePart += if (it.type.isSerializable) {
            "\${${it.type.navType.simpleName}.serializeValue(${it.name})}"
        } else if (it.type.isParcelable) {
            "\${${it.type.navType.simpleName}.serializeValue(${it.name})}"
        } else {
            "$${it.name}"
        }
        argumentRoutePart
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

fun getDefaultParametersImports(arguments: List<FunctionParameter>): String {
    return arguments.filter { it.hasDefault && it.defaultValue != null }
        .flatMap { it.defaultValue!!.imports }.joinToString(separator = "\n")
}

fun getDeepLinksCode(deepLinks: List<DeepLink>): String {
    if (deepLinks.isEmpty()) {
        return "emptyList<NavDeepLink>()"
    }
    return "listOf(\n\t\t" + deepLinks.joinToString(separator = ",\n\t\t") {
        val uriPattern = if (it.uriPattern.isBlank()) null else "\"${it.uriPattern}\""
        val action = if (it.action.isBlank()) null else "\"${it.action}\""
        val mimeType = if (it.mimeType.isBlank()) null else "\"${it.mimeType}\""
        """navDeepLink {
            uriPattern = $uriPattern
            action = $action
            mimeType = $mimeType
        }"""
    } + "\n\t)"
}
