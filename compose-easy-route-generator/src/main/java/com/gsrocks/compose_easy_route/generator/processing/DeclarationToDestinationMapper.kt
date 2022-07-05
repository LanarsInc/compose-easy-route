package com.gsrocks.compose_easy_route.generator.processing

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.core.utils.empty
import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.utils.findAnnotation
import com.gsrocks.compose_easy_route.generator.utils.findArgumentValue
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.gsrocks.compose_easy_route.generator.model.FunctionParameter
import com.gsrocks.compose_easy_route.generator.model.NavType
import com.gsrocks.compose_easy_route.generator.model.ParamType

class DeclarationToDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger
) {
    fun map(composableDestinations: Sequence<KSFunctionDeclaration>): List<DestinationWithParams> {
        return composableDestinations.map { it.toDestination() }.toList()
    }

    private fun KSFunctionDeclaration.toDestination(): DestinationWithParams {
        val destinationAnnotation = findAnnotation(Destination::class.simpleName!!)
        val routeName =
            destinationAnnotation.findArgumentValue<String>(Constants.ROUTE_NAME_PARAM)!!

        return DestinationWithParams(
            composableName = simpleName.asString(),
            composableQualifiedName = qualifiedName?.asString() ?: String.empty,
            routeName = routeName,
            parameters = parameters.filter { !it.hasDefault }.map { it.toFunctionParam() }
        )
    }

    private fun KSValueParameter.toFunctionParam(): FunctionParameter {
        val resolvedType = type.resolve()
        val resolvedTypeDeclaration = resolvedType.declaration

        val serializableType =
            resolver.getClassDeclarationByName("java.io.Serializable")!!.asType(emptyList())

        var isSerializable = serializableType.isAssignableFrom(resolvedType)

        val navType = try {
            val primitiveNavType =
                NavType.forType(resolvedTypeDeclaration.qualifiedName!!.asString())
            isSerializable = false
            primitiveNavType
        } catch (e: Exception) {
            if (isSerializable) {
                NavType.SerializableNavType(resolvedTypeDeclaration.simpleName.asString())
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
                isSerializable = isSerializable
            ),
            hasDefault = hasDefault
        )
    }
}
