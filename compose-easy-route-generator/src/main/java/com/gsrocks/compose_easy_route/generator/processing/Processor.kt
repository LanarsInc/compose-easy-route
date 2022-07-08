package com.gsrocks.compose_easy_route.generator.processing

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.gsrocks.compose_easy_route.core.annotation.Destination
import com.gsrocks.compose_easy_route.core.annotation.NavGraph
import com.gsrocks.compose_easy_route.core.annotation.RootNavGraph
import com.gsrocks.compose_easy_route.generator.generation.MainGenerator
import com.gsrocks.compose_easy_route.generator.model.DestinationWithParams
import com.gsrocks.compose_easy_route.generator.model.NavGraphInfo
import com.gsrocks.compose_easy_route.generator.model.NavGraphNode
import com.gsrocks.compose_easy_route.generator.utils.hasAnnotation

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
            startRoute = "",
            parentQualifiedName = "",
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
            startRoute = root.startRoute,
            destinations = destinations.filter { it.navGraphNode == null }.toList(),
            isRoot = true,
            nestedGraphs = processNestedGraphNodes(
                navGraphsNodes = navGraphsNodes,
                destinations = destinations,
                parentQualifiedName = root.qualifiedName
            )
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
            NavGraphInfo(
                route = node.route,
                simpleName = node.simpleName,
                qualifiedName = node.qualifiedName,
                startRoute = node.startRoute,
                destinations = destinations.filter { it.navGraphNode == node }.toList(),
                isRoot = node.isRoot,
                nestedGraphs = processNestedGraphNodes(
                    navGraphsNodes = navGraphsNodes,
                    destinations = destinations,
                    parentQualifiedName = node.qualifiedName
                )
            )
        }.toList()
    }
}
