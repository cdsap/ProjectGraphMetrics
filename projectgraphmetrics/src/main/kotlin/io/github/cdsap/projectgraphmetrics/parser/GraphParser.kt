package io.github.cdsap.projectgraphmetrics.parser

import io.github.cdsap.projectgraphmetrics.metrics.GraphMetricsCalculator
import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph

class GraphParser(private val result: SimpleDirectedGraph<String, DefaultEdge>) {

    constructor(fileGraph: String) : this(DotGraphLoader().load(fileGraph))

    fun result() = result

    fun getIndicatorsByModule(): Map<String, GraphMetric> {
        return GraphMetricsCalculator(result).getIndicatorsByModule()
    }
}
