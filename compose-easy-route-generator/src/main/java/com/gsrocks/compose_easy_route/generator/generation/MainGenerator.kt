package com.gsrocks.compose_easy_route.generator.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.gsrocks.compose_easy_route.generator.generation.writer.DestinationWriter
import com.gsrocks.compose_easy_route.generator.generation.writer.NavGraphWriter
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.gsrocks.compose_easy_route.generator.model.NavGraphInfo
import com.gsrocks.compose_easy_route.generator.model.NestedGraph

class MainGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    fun generate(
        destinations: List<DestinationWithParams>,
        nestedGraphs: List<NestedGraph>,
        startRoute: String
    ) {
        val destinationWriter = DestinationWriter(codeGenerator, logger)
        val navGraphWriter = NavGraphWriter(codeGenerator, logger)

        for (destination in destinations) {
            destinationWriter.write(destination)
        }

        val navGraphInfo = NavGraphInfo(destinations, nestedGraphs, startRoute)
        navGraphWriter.write(navGraphInfo)
    }
}
