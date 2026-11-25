package io.github.cdsap.projectgraphmetrics.cli.view

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.junit.Assert.assertEquals
import org.junit.Test

class CsvReportRendererTest {

    @Test
    fun emptyInputProducesOnlyHeaderWithoutTrailingNewline() {
        assertEquals(
            "Module,Indegree,Outdegree,BetweennessCentrality,Height",
            CsvReportRenderer.render(emptyMap())
        )
    }

    @Test
    fun modulesAreSortedWithExactColumnsNumbersAndWhitespace() {
        val modules = mapOf(
            ":feature" to GraphMetric(height = 2, betweennessCentrality = 1.5, indegree = 1, outdegree = 1),
            ":core" to GraphMetric(height = 1, betweennessCentrality = 0.0, indegree = 2, outdegree = 0),
            ":app" to GraphMetric(height = 3, betweennessCentrality = 0.5, indegree = 0, outdegree = 2)
        )

        assertEquals(
            "Module,Indegree,Outdegree,BetweennessCentrality,Height\n" +
                ":app,0,2,0.5,3\n" +
                ":core,2,0,0.0,1\n" +
                ":feature,1,1,1.5,2",
            CsvReportRenderer.render(modules)
        )
    }

    @Test
    fun numericRepresentationPreservesScientificNotationAndNegativeZero() {
        val modules = mapOf(
            ":small" to GraphMetric(height = 0, betweennessCentrality = 1.0E-4, indegree = 0, outdegree = 0),
            ":zero" to GraphMetric(height = 0, betweennessCentrality = -0.0, indegree = 0, outdegree = 0)
        )

        assertEquals(
            "Module,Indegree,Outdegree,BetweennessCentrality,Height\n" +
                ":small,0,0,1.0E-4,0\n" +
                ":zero,0,0,-0.0,0",
            CsvReportRenderer.render(modules)
        )
    }
}
