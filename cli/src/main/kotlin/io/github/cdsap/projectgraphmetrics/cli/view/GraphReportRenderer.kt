package io.github.cdsap.projectgraphmetrics.cli.view

import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import io.github.cdsap.projectgraphmetrics.model.GraphMetric

class GraphReportRenderer(private val modules: Map<String, GraphMetric>) {

    fun renderTopTenTable(): Table =
        table {
            cellStyle {
                border = true
                alignment = TextAlignment.MiddleLeft
                paddingLeft = 1
                paddingRight = 1
            }
            body {
                row {
                    cell("Top Ten Module Report") {
                        columnSpan = 8
                        alignment = TextAlignment.MiddleCenter
                    }
                }
                row {
                    cell("Indegree") {
                        columnSpan = 2
                    }
                    cell("Outdegree") {
                        columnSpan = 2
                    }
                    cell("BetweennessCentrality") {
                        columnSpan = 2
                    }
                    cell("Height") {
                        columnSpan = 2
                    }
                }
                val topIndegree =
                    modules.entries.sortedBy { it.value.indegree }.reversed()
                        .associate { it.toPair() }.entries.take(10)
                val topOutdegree =
                    modules.entries.sortedBy { it.value.outdegree }.reversed()
                        .associate { it.toPair() }.entries.take(10)
                val topHeight =
                    modules.entries.sortedBy { it.value.height }.reversed().associate { it.toPair() }.entries.take(
                        10
                    )
                val topBC = modules.entries.sortedBy { it.value.betweennessCentrality }.reversed()
                    .associate { it.toPair() }.entries.take(10)
                var i = 0
                topIndegree.forEach {
                    row {
                        cell(topIndegree.get(i).key)
                        cell(topIndegree.get(i).value.indegree)
                        cell(topOutdegree.get(i).key)
                        cell(topOutdegree.get(i).value.outdegree)
                        cell(topBC.get(i).key)
                        cell(topBC.get(i).value.betweennessCentrality)
                        cell(topHeight.get(i).key)
                        cell(topHeight.get(i).value.height)
                        i++
                    }
                }
            }
        }

    fun renderTopTenText(): String = renderTopTenTable().renderText()

    fun renderModulesText(): String =
        table {
            cellStyle {
                border = true
                alignment = TextAlignment.MiddleLeft
                paddingLeft = 1
                paddingRight = 1
            }
            body {
                row {
                    cell("Module")
                    cell("Indegree")
                    cell("Outdegree")
                    cell("BetweennessCentrality")
                    cell("Height")
                }
                modules.toSortedMap().forEach {
                    row {
                        cell(it.key)
                        cell(it.value.indegree) {
                            alignment = TextAlignment.MiddleRight
                        }
                        cell(it.value.outdegree) {
                            alignment = TextAlignment.MiddleRight
                        }
                        cell(it.value.betweennessCentrality) {
                            alignment = TextAlignment.MiddleRight
                        }
                        cell(it.value.height) {
                            alignment = TextAlignment.MiddleRight
                        }
                    }
                }
            }
        }.renderText()

    fun renderCsv(): String {
        val headers = "Module,Indegree,Outdegree,BetweennessCentrality,Height\n"
        var values = ""
        modules.toSortedMap().forEach {
            values += "${it.key},${it.value.indegree},${it.value.outdegree},${it.value.betweennessCentrality},${it.value.height}\n"
        }
        return """$headers$values""".trimIndent()
    }
}
