package com.gsrocks.compose_easy_route.generator.processing

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.core.annotation.NavGraph
import com.gsrocks.compose_easy_route.generator.constants.Constants
import com.gsrocks.compose_easy_route.generator.generation.MainGenerator
import com.gsrocks.compose_easy_route.generator.model.NestedGraph
import com.gsrocks.compose_easy_route.generator.utils.findAnnotation
import com.gsrocks.compose_easy_route.generator.utils.findArgumentValue
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

        val navGraphAnnotations = resolver.getNavGraphAnnotations()
        val nestedGraphs = navGraphAnnotations.map {
            val annotation = it.findAnnotation(NavGraph::class.simpleName!!)
            val route =
                annotation.findArgumentValue<String>(Constants.ROUTE_PARAM)!!
            val startRoute = annotation.findArgumentValue<String>(Constants.START_ROUTE_PARAM)!!
            NestedGraph(
                route = route,
                simpleName = it.simpleName.asString(),
                qualifiedName = it.qualifiedName!!.asString(),
                startRoute = startRoute
            )
        }

        val declarationToDestinationMapper = DeclarationToDestinationMapper(resolver, logger)
        val destinationsWithParams =
            declarationToDestinationMapper.map(annotatedDestinations, nestedGraphs)

        val generator = MainGenerator(codeGenerator, logger)

        generator.generate(destinationsWithParams, nestedGraphs.toList(), "")

        return emptyList()
    }

    private fun Resolver.getComposableDestinations(): Sequence<KSFunctionDeclaration> {
        return getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { it.hasAnnotation("Composable") }
    }

    private fun Resolver.getNavGraphAnnotations(): Sequence<KSClassDeclaration> {
        return getSymbolsWithAnnotation(NavGraph::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
    }
}
