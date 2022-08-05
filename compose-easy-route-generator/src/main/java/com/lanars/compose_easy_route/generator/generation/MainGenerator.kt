package com.lanars.compose_easy_route.generator.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.lanars.compose_easy_route.generator.generation.writer.DestinationWriter
import com.lanars.compose_easy_route.generator.generation.writer.NavGraphWriter
import com.lanars.compose_easy_route.generator.generation.writer.NavTypeWriter
import com.lanars.compose_easy_route.generator.model.DestinationWithParams
import com.lanars.compose_easy_route.generator.model.NavGraphInfo

class MainGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    fun generate(navGraphs: NavGraphInfo, destinations: Sequence<DestinationWithParams>) {
        val navTypeWriter = NavTypeWriter(codeGenerator, logger)
        val destinationWriter = DestinationWriter(codeGenerator, logger)
        val navGraphWriter = NavGraphWriter(codeGenerator, logger)

        navTypeWriter.write(destinations)

        for (destination in destinations) {
            destinationWriter.write(destination)
        }

        navGraphWriter.write(navGraphs)
    }
}
