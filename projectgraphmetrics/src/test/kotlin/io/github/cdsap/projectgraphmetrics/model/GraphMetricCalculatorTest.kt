package io.github.cdsap.projectgraphmetrics.model

import io.github.cdsap.projectgraphmetrics.parser.DotGraphLoader
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class GraphMetricCalculatorTest {

    @Test
    fun metricsFromInMemoryGraphMatchExpectedValues() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":app")
        graph.addVertex(":feature")
        graph.addVertex(":core")
        graph.addEdge(":app", ":feature")
        graph.addEdge(":feature", ":core")

        val metrics = GraphMetricCalculator(graph).getIndicatorsByModule()

        assertEquals(2, metrics[":app"]?.height)
        assertEquals(1, metrics[":feature"]?.height)
        assertEquals(0, metrics[":core"]?.height)
        assertEquals(1, metrics[":app"]?.outdegree)
        assertEquals(0, metrics[":app"]?.indegree)
        assertEquals(0.0, metrics[":app"]?.betweennessCentrality)
        assertEquals(1.0, metrics[":feature"]?.betweennessCentrality)
    }

    @Test
    fun heightUsesTheSuppliedDependencyModel() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":app")
        graph.addVertex(":feature")
        graph.addVertex(":core")
        graph.addEdge(":app", ":feature")
        graph.addEdge(":feature", ":core")

        val metrics = GraphMetricCalculator(
            graph,
            listOf(":app" to ":core")
        ).getIndicatorsByModule()

        assertEquals(1, metrics[":app"]?.height)
        assertEquals(-1, metrics[":feature"]?.height)
        assertEquals(0, metrics[":core"]?.height)
    }

    @Test
    fun graphOnlyCalculatorUsesGraphEdgeEndpoints() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        val from = ":feature:with : delimiter"
        val to = ":core:with : delimiter"
        graph.addVertex(from)
        graph.addVertex(to)
        graph.addEdge(from, to)

        val metrics = GraphMetricCalculator(graph).getIndicatorsByModule()

        assertEquals(1, metrics[from]?.height)
        assertEquals(0, metrics[to]?.height)
    }

    @Test
    fun metricsFromLoadedDotFileMatchExpectedValues() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = DotGraphLoader().load(file.path)

        val metrics = GraphMetricCalculator(graph).getIndicatorsByModule()

        assertEquals(1, metrics[":layer_1:module_1_54"]?.height)
        assertEquals(4, metrics[":layer_1:module_1_54"]?.outdegree)
        assertEquals(10, metrics[":layer_1:module_1_54"]?.indegree)
        assertEquals(14.44, metrics[":layer_1:module_1_54"]?.betweennessCentrality)
    }
}
