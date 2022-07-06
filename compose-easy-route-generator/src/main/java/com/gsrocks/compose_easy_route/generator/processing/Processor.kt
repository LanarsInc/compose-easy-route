package com.gsrocks.compose_easy_route.generator.processing

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.generator.generation.MainGenerator
import com.gsrocks.compose_easy_route.generator.utils.findAnnotation
import com.gsrocks.compose_easy_route.generator.utils.hasAnnotation

class Processor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedDestinations = resolver.getComposableDestinations()
        if (!annotatedDestinations.iterator().hasNext()) {
            return emptyList()
        }

        val declarationToDestinationMapper = DeclarationToDestinationMapper(resolver, logger)
        val destinationsWithParams = declarationToDestinationMapper.map(annotatedDestinations)

        val generator = MainGenerator(codeGenerator, logger)

        generator.generate(destinationsWithParams)

        return emptyList()
    }

    private fun Resolver.getComposableDestinations(): Sequence<KSFunctionDeclaration> {
        return getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { it.hasAnnotation("Composable") }
    }
}
