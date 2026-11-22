package io.github.cdsap.projectgraphmetrics.metrics

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import io.github.cdsap.projectgraphmetrics.parser.DotGraphReader
import org.junit.Assert.assertEquals
import org.junit.Test
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import java.io.File

class GraphMetricsCalculatorTest {

    @Test
    fun emptyGraphHasNoMetrics() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)

        assertEquals(emptyMap<String, GraphMetric>(), GraphMetricsCalculator(graph).getIndicatorsByModule())
    }

    @Test
    fun isolatedVertexPreservesUnknownHeightAndZeroMetrics() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":isolated")

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(
            mapOf(":isolated" to GraphMetric(height = -1, betweennessCentrality = 0.0, indegree = 0, outdegree = 0)),
            metrics
        )
    }

    @Test
    fun fractionalBetweennessIsRoundedToTwoDecimalPlaces() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":app")
        graph.addVertex(":core")
        val features = listOf(":feature1", ":feature2", ":feature3")
        features.forEach { feature ->
            graph.addVertex(feature)
            graph.addEdge(":app", feature)
            graph.addEdge(feature, ":core")
        }

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(GraphMetric(height = 2, betweennessCentrality = 0.0, indegree = 0, outdegree = 3), metrics[":app"])
        assertEquals(GraphMetric(height = 0, betweennessCentrality = 0.0, indegree = 3, outdegree = 0), metrics[":core"])
        features.forEach { feature ->
            assertEquals(GraphMetric(height = 1, betweennessCentrality = 0.33, indegree = 1, outdegree = 1), metrics[feature])
        }
    }

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
    fun heightUsesTheSuppliedDependencyModel() {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex(":app")
        graph.addVertex(":feature")
        graph.addVertex(":core")
        graph.addEdge(":app", ":feature")
        graph.addEdge(":feature", ":core")

        val metrics = GraphMetricsCalculator(
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

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(1, metrics[from]?.height)
        assertEquals(0, metrics[to]?.height)
    }

    @Test
    fun metricsFromLoadedDotFileMatchExpectedValues() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = DotGraphReader().read(file.path)

        val metrics = GraphMetricsCalculator(graph).getIndicatorsByModule()

        assertEquals(1, metrics[":layer_1:module_1_54"]?.height)
        assertEquals(4, metrics[":layer_1:module_1_54"]?.outdegree)
        assertEquals(10, metrics[":layer_1:module_1_54"]?.indegree)
        assertEquals(14.44, metrics[":layer_1:module_1_54"]?.betweennessCentrality)
    }
}
