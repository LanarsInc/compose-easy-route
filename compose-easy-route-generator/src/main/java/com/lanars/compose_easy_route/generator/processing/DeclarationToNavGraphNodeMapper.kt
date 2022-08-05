package com.lanars.compose_easy_route.generator.processing

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.lanars.compose_easy_route.core.annotation.NavGraph
import com.lanars.compose_easy_route.generator.constants.Constants
import com.lanars.compose_easy_route.generator.model.NavGraphNode
import com.lanars.compose_easy_route.generator.utils.findAnnotation
import com.lanars.compose_easy_route.generator.utils.findArgumentValue

class DeclarationToNavGraphNodeMapper(
    private val logger: KSPLogger
) {
    fun map(annotatedGraphs: Sequence<KSClassDeclaration>): Sequence<NavGraphNode> {
        return annotatedGraphs.map { it.toNavGraphNode() }
    }

    private fun KSClassDeclaration.toNavGraphNode(): NavGraphNode {
        val navGraphAnnotation = findAnnotation(NavGraph::class.simpleName!!)
        val route = navGraphAnnotation.findArgumentValue<String>(Constants.ROUTE_PARAM)!!
        val parentClass = navGraphAnnotation.findArgumentValue<KSType>(Constants.PARENT_PARAM)!!
        return NavGraphNode(
            simpleName = simpleName.asString(),
            qualifiedName = qualifiedName!!.asString(),
            route = route,
            parentQualifiedName = parentClass.declaration.qualifiedName!!.asString()
        )
    }
}
