package com.gsrocks.compose_easy_route.generator.processing

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.core.utils.empty
import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.model.*
import com.gsrocks.compose_easy_route.generator.utils.findAnnotation
import com.gsrocks.compose_easy_route.generator.utils.findArgumentValue
import com.gsrocks.compose_easy_route.generator.utils.hasAnnotation

class DeclarationToDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger
) {
    fun map(
        composableDestinations: Sequence<KSFunctionDeclaration>,
        navGraphs: Sequence<NestedGraph>
    ): List<DestinationWithParams> {
        return composableDestinations.map { it.toDestination(navGraphs) }.toList()
    }

    private fun KSFunctionDeclaration.toDestination(navGraphs: Sequence<NestedGraph>): DestinationWithParams {
        val destinationAnnotation = findAnnotation(Destination::class.simpleName!!)
        val routeName =
            destinationAnnotation.findArgumentValue<String>(Constants.ROUTE_NAME_PARAM)!!
        val deepLinks =
            destinationAnnotation.findArgumentValue<ArrayList<KSAnnotation>>(Constants.DEEP_LINKS_PARAM)!!

        val nestedGraph = navGraphs.firstOrNull { hasAnnotation(it.simpleName) }

        return DestinationWithParams(
            composableName = simpleName.asString(),
            composableQualifiedName = qualifiedName?.asString() ?: String.empty,
            routeName = routeName,
            parameters = parameters.map { it.toFunctionParam() },
            deepLinks = deepLinks.map { it.toDeepLink() },
            nestedGraph = nestedGraph
        )
    }

    private fun KSAnnotation.toDeepLink(): DeepLink {
        return DeepLink(
            uriPattern = findArgumentValue("uriPattern")!!,
            action = findArgumentValue("action")!!,
            mimeType = findArgumentValue("mimeType")!!,
        )
    }

    private fun KSValueParameter.toFunctionParam(): FunctionParameter {
        val resolvedType = type.resolve()
        val resolvedTypeDeclaration = resolvedType.declaration

        val serializableType =
            resolver.getClassDeclarationByName("java.io.Serializable")!!.asType(emptyList())
        val parcelableType =
            resolver.getClassDeclarationByName("android.os.Parcelable")!!.asType(emptyList())

        var isSerializable = serializableType.isAssignableFrom(resolvedType)
        var isParcelable = parcelableType.isAssignableFrom(resolvedType)

        val navType = try {
            val primitiveNavType =
                NavType.forType(resolvedTypeDeclaration.qualifiedName!!.asString())
            isSerializable = false
            isParcelable = false
            primitiveNavType
        } catch (e: Exception) {
            if (isSerializable) {
                NavType.SerializableNavType(resolvedTypeDeclaration.simpleName.asString())
            } else if (isParcelable) {
                NavType.ParcelableNavType(resolvedTypeDeclaration.simpleName.asString())
            } else {
                throw e
            }
        }

        return FunctionParameter(
            name = name?.asString()!!,
            type = ParamType(
                simpleName = resolvedTypeDeclaration.simpleName.asString(),
                qualifiedName = resolvedTypeDeclaration.qualifiedName!!.asString(),
                navType = navType,
                isSerializable = isSerializable,
                isParcelable = isParcelable
            ),
            hasDefault = hasDefault,
            defaultValue = getDefaultValue(resolver)
        )
    }
}
