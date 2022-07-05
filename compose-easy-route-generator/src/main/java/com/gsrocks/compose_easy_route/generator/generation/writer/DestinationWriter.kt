package com.gsrocks.compose_easy_route.generator.generation.writer

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.generation.template.getDestinationNoParamsTemplate
import com.gsrocks.compose_easy_route.generator.generation.template.getDestinationWthParamsTemplate
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class DestinationWriter(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    fun write(destination: DestinationWithParams) {
        val destinationClassName =
            destination.composableName + Constants.DESTINATION_NAME_SUFFIX
        val fileBuilder = FileSpec.scriptBuilder(
            destinationClassName,
            Constants.BASE_PACKAGE_NAME
        )
        val sourceCode =
            if (destination.parameters.isEmpty()) {
                getDestinationNoParamsTemplate(destination)
            } else {
                getDestinationWthParamsTemplate(destination)
            }

        fileBuilder.addCode(sourceCode)

        fileBuilder.build().writeTo(
            codeGenerator,
            Dependencies.ALL_FILES
        )
    }
}