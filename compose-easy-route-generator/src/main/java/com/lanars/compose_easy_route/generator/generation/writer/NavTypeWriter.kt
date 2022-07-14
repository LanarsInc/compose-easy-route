package com.lanars.compose_easy_route.generator.generation.writer

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.generation.template.navtype.getParcelableArrayNavTypeTemplate
import com.lanars.compose_easy_route.generator.generation.template.navtype.getSerializableArrayNavTypeTemplate
import com.lanars.compose_easy_route.generator.model.DestinationWithParams
import com.lanars.compose_easy_route.generator.model.NavType
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class NavTypeWriter(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    fun write(destinations: Sequence<DestinationWithParams>) {
        val serializableArrayTypes = destinations
            .flatMap { it.parameters }
            .filter { it.type.genericType != null && it.type.navType is NavType.SerializableArrayNavType }
            .map { it.type }
            .distinct()
        val parcelableArrayTypes = destinations
            .flatMap { it.parameters }
            .filter { it.type.genericType != null && it.type.navType is NavType.ParcelableArrayNavType }
            .map { it.type }
            .distinct()

        serializableArrayTypes.forEach {
            val fileBuilder = FileSpec.scriptBuilder(
                it.navType.simpleName,
                Constants.BASE_PACKAGE_NAME
            )
            val sourceCode = getSerializableArrayNavTypeTemplate(it.genericType!!)

            fileBuilder.addCode(sourceCode)

            fileBuilder.build().writeTo(
                codeGenerator,
                Dependencies.ALL_FILES
            )
        }

        parcelableArrayTypes.forEach {
            val fileBuilder = FileSpec.scriptBuilder(
                it.navType.simpleName,
                Constants.BASE_PACKAGE_NAME
            )
            val sourceCode = getParcelableArrayNavTypeTemplate(it.genericType!!)

            fileBuilder.addCode(sourceCode)

            fileBuilder.build().writeTo(
                codeGenerator,
                Dependencies.ALL_FILES
            )
        }
    }
}
