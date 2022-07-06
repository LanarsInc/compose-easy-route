package com.gsrocks.compose_easy_route.generator.generation.writer

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.model.NavGraphInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo

class NavGraphWriter(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    fun write(navGraphInfo: NavGraphInfo) {
        val fileBuilder = FileSpec.scriptBuilder(
            "NavGraphs",
            Constants.BASE_PACKAGE_NAME
        )

        val navGraphInterfaceType =
            ClassName("${Constants.BASE_PACKAGE_NAME}.navigation", "NavigationGraph")
        val destinationType =
            ClassName("${Constants.BASE_PACKAGE_NAME}.navigation", "NavDestination")
        val listType = ClassName("kotlin.collections", "List")
        val listOfDestinationsType = listType.parameterizedBy(destinationType)

        val destinationsInitializer = navGraphInfo.destinations.joinToString(",\n\t") {
            it.composableName + Constants.DESTINATION_NAME_SUFFIX
        }

        fileBuilder.addType(
            TypeSpec.objectBuilder("RootNavGraph")
                .addSuperinterface(navGraphInterfaceType)
                .addProperty(
                    PropertySpec.builder("destinations", listOfDestinationsType)
                        .mutable(false)
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("listOf(\n\t$destinationsInitializer\n)")
                        .build()
                ).build()
        )

        fileBuilder.build().writeTo(
            codeGenerator,
            Dependencies.ALL_FILES
        )
    }
}
