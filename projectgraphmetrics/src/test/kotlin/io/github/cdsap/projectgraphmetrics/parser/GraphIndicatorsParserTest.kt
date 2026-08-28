package io.github.cdsap.projectgraphmetrics.parser

import io.github.cdsap.projectgraphmetrics.ProjectGraphMetrics
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class GraphIndicatorsParserTest {

    @Test
    fun testModulesAreParsedCorrectly() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = ProjectGraphMetrics(file)
        val graphIndicatorsParser = graph.getMetrics()
        assertTrue(graphIndicatorsParser.containsKey(":layer_1:module_1_54"))
        assertTrue(graphIndicatorsParser.containsKey(":layer_2:module_2_85"))
        assertTrue(graphIndicatorsParser.containsKey(":layer_3:module_3_102"))
        assertTrue(graphIndicatorsParser.containsKey(":layer_4:module_4_115"))
        assertTrue(graphIndicatorsParser.containsKey(":layer_5:module_5_121"))
    }

    @Test
    fun testMetricsByModuleAreParsedCorrectly() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = ProjectGraphMetrics(file)
        val graphIndicatorsParser = graph.getMetrics()
        assertEquals(1, graphIndicatorsParser[":layer_1:module_1_54"]?.height)
        assertEquals(4, graphIndicatorsParser[":layer_1:module_1_54"]?.outdegree)
        assertEquals(10, graphIndicatorsParser[":layer_1:module_1_54"]?.indegree)
        assertEquals(14.44, graphIndicatorsParser[":layer_1:module_1_54"]?.betweennessCentrality)
    }

    @Test
    fun testMissingGraphFileThrowsFileNotFoundException() {
        val missingFile = "missing-graph.dot"
        val exception = assertThrows(FileNotFoundException::class.java) {
            GraphParser(missingFile).getIndicatorsByModule()
        }
        assertEquals("$missingFile not found", exception.message)
    }

    @Test
    fun testDotGraphLoaderLoadsDotFile() {
        val file = File(this::class.java.classLoader!!.getResource("graph.dot")?.path)
        val graph = DotGraphLoader().load(file.path)
        assertTrue(graph.containsVertex(":layer_1:module_1_54"))
        assertEquals(4, graph.outDegreeOf(":layer_1:module_1_54"))
        assertEquals(10, graph.inDegreeOf(":layer_1:module_1_54"))
    }
}
