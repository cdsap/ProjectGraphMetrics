package io.github.cdsap.projectgraphmetrics.model

data class GraphMetric(
    val height: Int,
    val betweennessCentrality: Double,
    val indegree: Int,
    val outdegree: Int
)
