package com.lanars.compose_easy_route.generator.processing

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.core.annotation.ParentBackStackEntry
import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.model.*
import com.lanars.compose_easy_route.generator.utils.findAnnotation
import com.lanars.compose_easy_route.generator.utils.findArgumentValue
import com.lanars.compose_easy_route.generator.utils.hasAnnotation

class DeclarationToDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger
) {
    private val serializableType by lazy {
        resolver.getClassDeclarationByName("java.io.Serializable")!!.asType(emptyList())
    }
    private val parcelableType by lazy {
        resolver.getClassDeclarationByName("android.os.Parcelable")!!.asType(emptyList())
    }

    fun map(
        composableDestinations: Sequence<KSFunctionDeclaration>,
        navGraphs: Sequence<NavGraphNode>
    ): Sequence<DestinationWithParams> {
        return composableDestinations.map { it.toDestination(navGraphs) }
    }

    private fun KSFunctionDeclaration.toDestination(navGraphs: Sequence<NavGraphNode>): DestinationWithParams {
        val destinationAnnotation = findAnnotation(Destination::class.simpleName!!)
        val routeName =
            destinationAnnotation.findArgumentValue<String>(Constants.ROUTE_NAME_PARAM)!!
        val deepLinks =
            destinationAnnotation.findArgumentValue<ArrayList<KSAnnotation>>(Constants.DEEP_LINKS_PARAM)!!

        val nestedGraph = navGraphs.firstOrNull { hasAnnotation(it.simpleName) }
        val graphAnnotation = nestedGraph?.let { findAnnotation(it.simpleName) }
        val isStart = graphAnnotation?.findArgumentValue<Boolean>(Constants.START_PARAM) ?: false

        val nonAnnotatedParams = parameters.filter {
            !it.hasAnnotation(ParentBackStackEntry::class.simpleName!!)
        }
        return DestinationWithParams(
            composableName = simpleName.asString(),
            composableQualifiedName = qualifiedName?.asString() ?: String.empty,
            routeName = routeName,
            parameters = nonAnnotatedParams.map { it.toFunctionParam() },
            deepLinks = deepLinks.map { it.toDeepLink() },
            navGraphNode = nestedGraph,
            backStackEntryParamName = parameters.firstOrNull {
                it.hasAnnotation(ParentBackStackEntry::class.simpleName!!)
            }?.name?.asString(),
            isStart = isStart
        )
    }

    private fun KSAnnotation.toDeepLink(): DeepLink {
        return DeepLink(
            uriPattern = findArgumentValue(Constants.URI_PATTERN_PARAM)!!,
            action = findArgumentValue(Constants.ACTION_PARAM)!!,
            mimeType = findArgumentValue(Constants.MIME_TYPE)!!,
        )
    }

    private fun KSValueParameter.toFunctionParam(): FunctionParameter {
        val resolvedType = type.resolve()
        val resolvedTypeDeclaration = resolvedType.declaration

        var isSerializable = serializableType.isAssignableFrom(resolvedType.makeNotNullable())
        var isParcelable = parcelableType.isAssignableFrom(resolvedType.makeNotNullable())
        val isArray =
            resolvedTypeDeclaration.qualifiedName?.asString() == Array::class.qualifiedName
        val isEnum = (resolvedTypeDeclaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS

        val navType = try {
            val primitiveNavType = if (isArray) {
                val genericTypeQualifiedName = resolvedType.getGenericArgumentType()?.qualifiedName
                genericTypeQualifiedName?.let { NavType.forArrayType(it) }
                    ?: throw IllegalArgumentException()
            } else if (isEnum) {
                NavType.EnumNavType(resolvedTypeDeclaration.simpleName.asString())
            } else {
                NavType.forType(resolvedTypeDeclaration.qualifiedName!!.asString())
            }
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
                isParcelable = isParcelable,
                genericType = resolvedType.getGenericArgumentType(),
                isNullable = resolvedType.isMarkedNullable,
                isEnum = isEnum
            ),
            hasDefault = hasDefault,
            defaultValue = getDefaultValue(resolver),
        )
    }

    private fun KSType.getGenericArgumentType(): GenericType? {
        val typeArgument = arguments.firstOrNull()
        val resolvedType = typeArgument?.type?.resolve()
        val resolvedTypeDeclaration = resolvedType?.declaration ?: return null
        return GenericType(
            simpleName = resolvedTypeDeclaration.simpleName.asString(),
            qualifiedName = resolvedTypeDeclaration.qualifiedName!!.asString(),
            isSerializable = serializableType.isAssignableFrom(this),
            isParcelable = parcelableType.isAssignableFrom(this),
            isNullable = resolvedType.isMarkedNullable
        )
    }
}
