package com.lanars.compose_easy_route.generator.generation.writer

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.generation.template.getNavGraphCode
import com.lanars.compose_easy_route.generator.model.NavGraphInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo

class NavGraphWriter(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    private val navGraphInterfaceType =
        ClassName("${Constants.BASE_PACKAGE_NAME}.navigation", "NavigationGraph")

    fun write(navGraphs: NavGraphInfo) {
        val fileBuilder = FileSpec.builder(
            Constants.BASE_PACKAGE_NAME,
            "NavGraphs"
        )

        val objectBuilder = TypeSpec.objectBuilder("NavGraphs")

        val graphProperties = mutableListOf<PropertySpec>()
        writeNavGraph(navGraphs, graphProperties)
        graphProperties.reversed().forEach { objectBuilder.addProperty(it) }

        fileBuilder.addType(objectBuilder.build())

        fileBuilder.build().writeTo(
            codeGenerator,
            Dependencies.ALL_FILES
        )
    }

    private fun writeNavGraph(navGraph: NavGraphInfo, graphsProperties: MutableList<PropertySpec>) {
        val visibilityModifier = if (navGraph.isRoot) KModifier.PUBLIC else KModifier.PRIVATE
        graphsProperties.add(
            PropertySpec.builder(navGraph.route, navGraphInterfaceType)
                .addModifiers(visibilityModifier)
                .mutable(false)
                .initializer(getNavGraphCode(navGraph))
                .build()
        )

        navGraph.nestedGraphs.forEach { writeNavGraph(it, graphsProperties) }
    }
}
