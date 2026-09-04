package io.github.cdsap.projectgraphmetrics.parser

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.jgrapht.alg.scoring.BetweennessCentrality
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import java.text.DecimalFormat

class GraphParser(private val result: SimpleDirectedGraph<String, DefaultEdge>) {
    private val decimalFormat = DecimalFormat("#.##")
    private val betweennessCentrality: Map<String, Double>
    private val heightCalculator: GraphHeightCalculator

    constructor(fileGraph: String) : this(DotGraphLoader().load(fileGraph))

    init {
        val edgesParsed = result.edgeSet().map {
            it.toString().removeSurrounding("(", ")").replace(".", "_").split(" : ")
                .let { parts -> parts[0] to parts[1] }
        }
        heightCalculator = GraphHeightCalculator(edgesParsed)
        betweennessCentrality = BetweennessCentrality(result).scores
    }

    fun result() = result

    fun betweennessCentrality(module: String) =
        decimalFormat.format(betweennessCentrality[module] ?: 0.0).toDouble()

    fun inDegree(module: String) = try {
        result.inDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    fun outDegree(module: String) = try {
        result.outDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    fun heightOf(key: String): Int = heightCalculator.heightOf(key)

    fun getIndicatorsByModule(): Map<String, GraphMetric> {
        return result().vertexSet().associateWith {
            GraphMetric(
                height = heightOf(it),
                indegree = inDegree(it),
                outdegree = outDegree(it),
                betweennessCentrality = betweennessCentrality(it)
            )
        }
    }
}
