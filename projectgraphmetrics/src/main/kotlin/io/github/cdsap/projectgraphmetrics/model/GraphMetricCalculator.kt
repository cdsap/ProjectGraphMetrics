package io.github.cdsap.projectgraphmetrics.model

import io.github.cdsap.projectgraphmetrics.metrics.GraphHeightCalculator
import org.jgrapht.alg.scoring.BetweennessCentrality
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import java.text.DecimalFormat

private fun SimpleDirectedGraph<String, DefaultEdge>.dependencyEdges(): List<Pair<String, String>> =
    edgeSet().map { edge ->
        getEdgeSource(edge) to getEdgeTarget(edge)
    }

class GraphMetricCalculator(
    private val graph: SimpleDirectedGraph<String, DefaultEdge>,
    dependencyEdges: List<Pair<String, String>>
) {
    constructor(graph: SimpleDirectedGraph<String, DefaultEdge>) : this(
        graph,
        graph.dependencyEdges()
    )

    private val decimalFormat = DecimalFormat("#.##")
    private val betweennessCentrality: Map<String, Double>
    private val heightCalculator: GraphHeightCalculator

    init {
        heightCalculator = GraphHeightCalculator(dependencyEdges.map { (from, to) ->
            normalizeModule(from) to normalizeModule(to)
        })
        betweennessCentrality = BetweennessCentrality(graph).scores
    }

    fun getIndicatorsByModule(): Map<String, GraphMetric> {
        return graph.vertexSet().associateWith {
            GraphMetric(
                height = heightOf(it),
                indegree = inDegree(it),
                outdegree = outDegree(it),
                betweennessCentrality = betweennessCentrality(it)
            )
        }
    }

    private fun normalizeModule(module: String) = module.replace(".", "_")

    private fun betweennessCentrality(module: String) =
        decimalFormat.format(betweennessCentrality[module] ?: 0.0).toDouble()

    private fun inDegree(module: String) = try {
        graph.inDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    private fun outDegree(module: String) = try {
        graph.outDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    private fun heightOf(key: String): Int = heightCalculator.heightOf(normalizeModule(key))
}
