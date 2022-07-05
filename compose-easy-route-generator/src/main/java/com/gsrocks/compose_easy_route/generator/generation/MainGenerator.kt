package com.gsrocks.compose_easy_route.generator.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.gsrocks.compose_easy_route.generator.generation.writer.DestinationWriter
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams

class MainGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    fun generate(destinations: List<DestinationWithParams>) {
        val destinationWriter = DestinationWriter(codeGenerator, logger)

        for (destination in destinations) {
            destinationWriter.write(destination)
        }
    }
}