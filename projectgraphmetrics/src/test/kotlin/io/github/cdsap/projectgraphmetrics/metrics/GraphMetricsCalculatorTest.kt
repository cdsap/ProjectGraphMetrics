package io.github.cdsap.projectgraphmetrics.metrics

import io.github.cdsap.projectgraphmetrics.parser.DotGraphLoader
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class GraphMetricsCalculatorTest {

    @Test
    fun metricsFromInMemoryGraphMatchExpectedValues() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":app")
        graph.addVertex(":feature")
        graph.addVertex(":core")
        graph.addEdge(":app", ":feature")
        graph.addEdge(":feature", ":core")

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(2, metrics[":app"]?.height)
        assertEquals(1, metrics[":feature"]?.height)
        assertEquals(0, metrics[":core"]?.height)
        assertEquals(1, metrics[":app"]?.outdegree)
        assertEquals(0, metrics[":app"]?.indegree)
        assertEquals(0.0, metrics[":app"]?.betweennessCentrality)
        assertEquals(1.0, metrics[":feature"]?.betweennessCentrality)
    }

    @Test
    fun metricsFromLoadedDotFileMatchExpectedValues() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = DotGraphLoader().load(file.path)

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(1, metrics[":layer_1:module_1_54"]?.height)
        assertEquals(4, metrics[":layer_1:module_1_54"]?.outdegree)
        assertEquals(10, metrics[":layer_1:module_1_54"]?.indegree)
        assertEquals(14.44, metrics[":layer_1:module_1_54"]?.betweennessCentrality)
    }
}
