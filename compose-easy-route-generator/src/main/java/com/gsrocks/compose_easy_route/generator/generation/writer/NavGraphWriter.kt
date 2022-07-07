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
        val nestedGraphType =
            ClassName("${Constants.BASE_PACKAGE_NAME}.navigation", "NestedGraphDeclaration")
        val listType = ClassName("kotlin.collections", "List")
        val listOfDestinationsType = listType.parameterizedBy(destinationType)

        navGraphInfo.nestedGraphs.forEach {
            fileBuilder.addImport(it.qualifiedName, "")
        }

        val rootDestinations = navGraphInfo.destinations.filter { it.nestedGraph == null }
        val rootDestinationsInitializer = rootDestinations.joinToString(",\n\t") {
            it.composableName + Constants.DESTINATION_NAME_SUFFIX
        }
        val nestedGraphsInitializer = navGraphInfo.nestedGraphs.joinToString(",\n\t") {
            it.route
        }

        val objectBuilder = TypeSpec.objectBuilder("NavGraphs")

        val destinationsByGraphs =
            navGraphInfo.destinations
                .filter { it.nestedGraph != null }
                .groupBy { it.nestedGraph!! }

        navGraphInfo.nestedGraphs.forEach {
            val destinationsInitializer = destinationsByGraphs[it]?.joinToString(",\n\t") { dest ->
                dest.composableName + Constants.DESTINATION_NAME_SUFFIX
            } ?: return@forEach

            objectBuilder.addProperty(
                PropertySpec.builder(it.route, navGraphInterfaceType)
                    .mutable(false)
                    .initializer(
                        "NavigationGraph(\n" +
                                "\troute = \"${it.route}\",\n" +
                                "\tstartRoute = \"${it.startRoute}\",\n" +
                                "\tdestinations = listOf(\n" +
                                "\t\t$destinationsInitializer\n" +
                                "\t)\n)"
                    )
                    .build()
            )
        }

        objectBuilder.addProperty(
            PropertySpec.builder("root", navGraphInterfaceType)
                .mutable(false)
                .initializer(
                    "NavigationGraph(\n" +
                            "\troute = \"root\",\n" +
                            "\tstartRoute = \"${navGraphInfo.startRoute}\",\n" +
                            "\tdestinations = listOf(\n" +
                            "\t\t$rootDestinationsInitializer\n" +
                            "\t),\n" +
                            "nestedGraphs = listOf(\n\t\t$nestedGraphsInitializer\n\t)," +
                            "\n)"
                )
                .build()
        )

        fileBuilder.addType(objectBuilder.build())

        fileBuilder.build().writeTo(
            codeGenerator,
            Dependencies.ALL_FILES
        )
    }
}
