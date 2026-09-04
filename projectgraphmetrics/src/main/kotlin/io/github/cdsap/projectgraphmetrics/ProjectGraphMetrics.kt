package io.github.cdsap.projectgraphmetrics

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import io.github.cdsap.projectgraphmetrics.parser.DotGraphLoader
import io.github.cdsap.projectgraphmetrics.parser.GraphParser
import java.io.File

class ProjectGraphMetrics(private val file: File) {

    fun getMetrics(): Map<String, GraphMetric> {
        val graph = DotGraphLoader().load(file.path)
        return GraphParser(graph).getIndicatorsByModule()
    }
}
