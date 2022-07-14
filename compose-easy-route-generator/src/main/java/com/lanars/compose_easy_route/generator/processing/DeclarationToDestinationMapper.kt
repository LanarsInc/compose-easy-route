package com.lanars.compose_easy_route.generator.processing

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.core.annotation.ParentBackStackEntry
import com.lanars.compose_easy_route.core.exception.UnsupportedNavArgumentType
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

        val navType = when {
            resolvedType.isPrimitive() -> NavType.forType(resolvedTypeDeclaration.qualifiedName!!.asString())
            resolvedType.isEnum() -> NavType.EnumNavType(resolvedTypeDeclaration.simpleName.asString())
            resolvedType.isPrimitiveArray() -> {
                val genericTypeQualifiedName = resolvedType.getGenericArgumentType()!!.qualifiedName
                genericTypeQualifiedName.let { NavType.forArrayType(it) }
            }
            resolvedType.isParcelableArray() -> {
                val genericTypeName = resolvedType.getGenericArgumentType()!!.simpleName
                NavType.ParcelableArrayNavType(genericTypeName)
            }
            resolvedType.isSerializableArray() -> {
                val genericTypeName = resolvedType.getGenericArgumentType()!!.simpleName
                NavType.SerializableArrayNavType(genericTypeName)
            }
            resolvedType.isParcelable() -> NavType.ParcelableNavType(resolvedTypeDeclaration.simpleName.asString())
            resolvedType.isSerializable() -> NavType.SerializableNavType(resolvedTypeDeclaration.simpleName.asString())
            else -> throw UnsupportedNavArgumentType()
        }

        return FunctionParameter(
            name = name?.asString()!!,
            type = ParamType(
                simpleName = resolvedTypeDeclaration.simpleName.asString(),
                qualifiedName = resolvedTypeDeclaration.qualifiedName!!.asString(),
                navType = navType,
                isSerializable = resolvedType.isSerializable(),
                isParcelable = resolvedType.isParcelable(),
                genericType = resolvedType.getGenericArgumentType(),
                isNullable = resolvedType.isMarkedNullable,
                isEnum = resolvedType.isEnum()
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
            isSerializable = serializableType.isAssignableFrom(resolvedType),
            isParcelable = parcelableType.isAssignableFrom(resolvedType),
            isNullable = resolvedType.isMarkedNullable
        )
    }

    private fun KSType.isPrimitive(): Boolean {
        return try {
            NavType.forType(declaration.qualifiedName!!.asString())
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun KSType.isSerializable(): Boolean {
        return serializableType.isAssignableFrom(this.makeNotNullable())
    }

    private fun KSType.isParcelable(): Boolean {
        return parcelableType.isAssignableFrom(this.makeNotNullable())
    }

    private fun KSType.isEnum(): Boolean {
        return (declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS
    }

    private fun KSType.isArray(): Boolean {
        return declaration.qualifiedName?.asString() == Array::class.qualifiedName
    }

    private fun KSType.isPrimitiveArray(): Boolean {
        if (isArray()) {
            val genericTypeQualifiedName = getGenericArgumentType()?.qualifiedName
            return genericTypeQualifiedName?.let {
                try {
                    NavType.forArrayType(it)
                } catch (e: Exception) {
                    null
                }
            } != null
        }
        return false
    }

    private fun KSType.isSerializableArray(): Boolean {
        if (isArray()) {
            val genericTypeQualifiedName = getGenericArgumentType()
            return genericTypeQualifiedName?.isSerializable ?: false
        }
        return false
    }

    private fun KSType.isParcelableArray(): Boolean {
        if (isArray()) {
            val generic = getGenericArgumentType()
            return generic?.isParcelable ?: false
        }
        return false
    }
}
