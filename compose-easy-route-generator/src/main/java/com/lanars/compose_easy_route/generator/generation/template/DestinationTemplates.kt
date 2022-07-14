package com.lanars.compose_easy_route.generator.generation.template

import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.model.DeepLink
import com.lanars.compose_easy_route.generator.model.DestinationWithParams
import com.lanars.compose_easy_route.generator.model.FunctionParameter

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
${getImports(destination.parameters)}

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
    override fun Content(
        backStackEntry: NavBackStackEntry,
        parentBackStackEntry: NavBackStackEntry
    ) {
        ${destination.composableName}(${getBackStackEntryCode(destination.backStackEntryParamName)})
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
${getImports(destination.parameters)}

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
            override val route = "${destination.routeName}"${getRouteArgumentsCode(destination.parameters)}
        }
    }
    
    @Composable
    override fun Content(
        backStackEntry: NavBackStackEntry,
        parentBackStackEntry: NavBackStackEntry
    ) {
        ${destination.composableName}(
            ${getContentArgumentsCode(destination)}
        )
    }
}
""".trimIndent()
}

fun getNavArgumentsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t") {
        var code = "navArgument(\"${it.name}\") { type = ${it.type.navType.simpleName}"
        if (it.hasDefault && it.defaultValue != null) {
            code += "\n\t\t\tdefaultValue = ${it.defaultValue.code}"
        }
        if (it.type.isNullable) {
            code += "\n\t\t\tnullable = true"
        }
        code += " },"
        code
    }
}

fun getInvokeParamsCode(arguments: List<FunctionParameter>): String {
    return arguments.joinToString(separator = "\n\t\t") {
        if (it.hasDefault && it.defaultValue != null) {
            "${it.name}: ${it.type.getTypeName()} = ${it.defaultValue.code},"
        } else {
            "${it.name}: ${it.type.getTypeName()},"
        }
    }
}

fun getRouteArgumentsCode(arguments: List<FunctionParameter>): String {
    if (arguments.isEmpty()) {
        return String.empty
    }

    val separator = " +\n\t\t\t\t"
    return separator + arguments.joinToString(separator = separator) {
        var argumentRoutePart =
            if (it.hasDefault && it.defaultValue != null) "?${it.name}=" else "/"
        argumentRoutePart += "\${${it.type.navType.simpleName}.serializeValue(${it.name})}"
        "\"$argumentRoutePart\""
    }
}

fun getContentArgumentsCode(destination: DestinationWithParams): String {
    var code = destination.parameters.joinToString(separator = "\n\t\t\t") {
        var arg = "${it.name} = ${it.type.navType.simpleName}.get(backStackEntry, \"${it.name}\")"
        if (!it.type.isNullable) {
            arg += "!!"
        }
        arg += ","
        return@joinToString arg
    }
    val backStackEntryCode = getBackStackEntryCode(destination.backStackEntryParamName)
    if (code.isNotEmpty() && backStackEntryCode.isNotEmpty()) {
        code += "\n\t\t\t"
    }
    code += backStackEntryCode
    return code
}

fun getImports(arguments: List<FunctionParameter>): String {
    val imports = arguments.filter { it.hasDefault && it.defaultValue != null }
        .flatMap { it.defaultValue!!.imports }.toMutableList()
    imports.addAll(arguments.map { "import " + it.type.navType.qualifiedName })
    imports.addAll(arguments.map { "import " + it.type.qualifiedName })
    imports.addAll(
        arguments
            .filter { it.type.genericType != null }
            .map { "import " + it.type.genericType!!.qualifiedName }
    )
    return imports.distinct().joinToString(separator = "\n")
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

fun getBackStackEntryCode(paramName: String?): String {
    if (paramName == null) return ""

    return "$paramName = parentBackStackEntry"
}
