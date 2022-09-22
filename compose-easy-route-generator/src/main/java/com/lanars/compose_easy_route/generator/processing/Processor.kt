package com.lanars.compose_easy_route.generator.processing

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.lanars.compose_easy_route.core.annotation.Destination
import com.lanars.compose_easy_route.core.annotation.NavGraph
import com.lanars.compose_easy_route.core.annotation.RootNavGraph
import com.lanars.compose_easy_route.core.utils.empty
import com.lanars.compose_easy_route.generator.generation.MainGenerator
import com.lanars.compose_easy_route.generator.model.DestinationWithParams
import com.lanars.compose_easy_route.generator.model.NavGraphInfo
import com.lanars.compose_easy_route.generator.model.NavGraphNode
import com.lanars.compose_easy_route.generator.utils.hasAnnotation

class Processor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val navGraphAnnotations = resolver.getNavGraphAnnotations()
        val navGraphNodesMapper = DeclarationToNavGraphNodeMapper(logger)
        val navGraphNodes = navGraphNodesMapper.map(navGraphAnnotations)

        val annotatedDestinations = resolver.getComposableDestinations()
        if (!annotatedDestinations.iterator().hasNext()) {
            return emptyList()
        }

        val declarationToDestinationMapper = DeclarationToDestinationMapper(resolver, logger)
        val destinationsWithParams = declarationToDestinationMapper.map(
            annotatedDestinations,
            navGraphNodes
        )

        val rootNode = NavGraphNode(
            simpleName = RootNavGraph::class.simpleName!!,
            qualifiedName = RootNavGraph::class.qualifiedName!!,
            route = "root",
            parentQualifiedName = String.empty,
            isRoot = true
        )

        val processedNavGraphInfo = createNavGraphNodes(
            navGraphsNodes = sequenceOf(rootNode) + navGraphNodes,
            destinations = destinationsWithParams
        )

        MainGenerator(codeGenerator, logger).generate(
            processedNavGraphInfo,
            destinationsWithParams
        )

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

    private fun createNavGraphNodes(
        navGraphsNodes: Sequence<NavGraphNode>,
        destinations: Sequence<DestinationWithParams>
    ): NavGraphInfo {
        val root = navGraphsNodes.single { it.isRoot }
        return NavGraphInfo(
            route = root.route,
            simpleName = root.simpleName,
            qualifiedName = root.qualifiedName,
            startRoute = String.empty,
            destinations = destinations.filter { it.navGraphNode == null }.toList(),
            isRoot = true,
            nestedGraphs = processNestedGraphNodes(
                navGraphsNodes = navGraphsNodes,
                destinations = destinations,
                parentQualifiedName = root.qualifiedName
            ),
        )
    }

    private fun processNestedGraphNodes(
        navGraphsNodes: Sequence<NavGraphNode>,
        destinations: Sequence<DestinationWithParams>,
        parentQualifiedName: String
    ): List<NavGraphInfo> {
        val children = navGraphsNodes.filter { it.parentQualifiedName == parentQualifiedName }

        if (!children.iterator().hasNext()) {
            return emptyList()
        }

        return children.map { node ->
            val thisGraphsDestinations = destinations.filter { it.navGraphNode == node }.toList()
            val startRoute = thisGraphsDestinations.singleOrNull { it.isStart }?.routeName
            require(node.isIndependent || startRoute != null) {
                "Exactly one destination must be marked as start"
            }
            NavGraphInfo(
                route = node.route,
                simpleName = node.simpleName,
                qualifiedName = node.qualifiedName,
                startRoute = startRoute ?: String.empty,
                destinations = thisGraphsDestinations,
                isRoot = node.isRoot,
                nestedGraphs = processNestedGraphNodes(
                    navGraphsNodes = navGraphsNodes,
                    destinations = destinations,
                    parentQualifiedName = node.qualifiedName
                ),
                isIndependent = node.isIndependent
            )
        }.toList()
    }
}
