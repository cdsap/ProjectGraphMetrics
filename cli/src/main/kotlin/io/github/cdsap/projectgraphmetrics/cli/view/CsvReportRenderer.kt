package io.github.cdsap.projectgraphmetrics.cli.view

import io.github.cdsap.projectgraphmetrics.model.GraphMetric

internal object CsvReportRenderer {
    fun render(modules: Map<String, GraphMetric>): String {
        val headers = "Module,Indegree,Outdegree,BetweennessCentrality,Height\n"
        var values = ""
        modules.toSortedMap().forEach {
            values += "${it.key},${it.value.indegree},${it.value.outdegree},${it.value.betweennessCentrality},${it.value.height}\n"
        }
        return """$headers$values""".trimIndent()
    }
}
