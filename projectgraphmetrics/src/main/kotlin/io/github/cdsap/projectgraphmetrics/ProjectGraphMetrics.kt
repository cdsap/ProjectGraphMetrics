package io.github.cdsap.projectgraphmetrics

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import io.github.cdsap.projectgraphmetrics.parser.GraphParser
import java.io.File

class ProjectGraphMetrics(private val file: File) {

    fun getMetrics(): Map<String, GraphMetric> {
        return GraphParser(file.path).getIndicatorsByModule()
    }
}
