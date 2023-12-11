package io.github.cdsap.projectgraphmetrics.cli.view

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import io.github.cdsap.projectgraphmetrics.cli.model.FilesOutput
import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import java.io.File

class GraphViewWriter(private val modules: Map<String, GraphMetric>) {

    private val filesOutput =
        FilesOutput(
            moduleReportCsv = "modules_report.csv",
            moduleReportTxt = "modules_report.txt",
            topTenModuleReportTxt = "top_ten_module_report.txt"
        )

    fun generate() {
        printTopTenIndicatorsConsole()
        writeTopTenIndicatorsTxt()
        writeTxt()
        writeCsv()
    }

    private fun writeTopTenIndicatorsTxt() {
        if (File(filesOutput.topTenModuleReportTxt).exists()) {
            File(filesOutput.topTenModuleReportTxt).delete()
        }
        File(filesOutput.topTenModuleReportTxt).writeText(topTenModules().renderText())
        if (File(filesOutput.topTenModuleReportTxt).exists()) {
            println("${filesOutput.topTenModuleReportTxt} created")
        }
    }

    private fun printTopTenIndicatorsConsole() {
        println(topTenModules())
    }

    private fun topTenModules() =
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

    private fun writeTxt() {
        if (File(filesOutput.moduleReportTxt).exists()) {
            File(filesOutput.moduleReportTxt).delete()
        }
        File(filesOutput.moduleReportTxt).writeText(
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
        )
        if (File(filesOutput.moduleReportTxt).exists()) {
            println("${filesOutput.moduleReportTxt} created")
        }
    }

    private fun writeCsv() {
        if (File(filesOutput.moduleReportCsv).exists()) {
            File(filesOutput.moduleReportCsv).delete()
        }
        val headers = "Module,Indegree,Outdegree,BetweennessCentrality,Height\n"
        var values = ""
        modules.toSortedMap().forEach {
            values += "${it.key},${it.value.indegree},${it.value.outdegree},${it.value.betweennessCentrality},${it.value.height}\n"
        }
        File(filesOutput.moduleReportCsv).writeText("""$headers$values""".trimIndent())
        if (File(filesOutput.moduleReportCsv).exists()) {
            println("${filesOutput.moduleReportCsv} created")
        }
    }
}
