package io.github.cdsap.projectgraphmetrics.metrics

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.jgrapht.alg.scoring.BetweennessCentrality
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import java.text.DecimalFormat

class GraphMetricsCalculator(
    private val graph: SimpleDirectedGraph<String, DefaultEdge>
) {
    private val decimalFormat = DecimalFormat("#.##")
    private val betweennessCentrality: Map<String, Double>
    private val heightCalculator: GraphHeightCalculator

    init {
        val edgesParsed = graph.edgeSet().map { edge ->
            graph.getEdgeSource(edge) to graph.getEdgeTarget(edge)
        }
        heightCalculator = GraphHeightCalculator(edgesParsed)
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

    private fun heightOf(key: String): Int = heightCalculator.heightOf(key)
}
