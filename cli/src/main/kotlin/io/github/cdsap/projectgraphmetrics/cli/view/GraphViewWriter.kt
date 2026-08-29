package io.github.cdsap.projectgraphmetrics.cli.view

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

    private val renderer = GraphReportRenderer(modules)

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
        File(filesOutput.topTenModuleReportTxt).writeText(renderer.renderTopTenText())
        if (File(filesOutput.topTenModuleReportTxt).exists()) {
            println("${filesOutput.topTenModuleReportTxt} created")
        }
    }

    private fun printTopTenIndicatorsConsole() {
        println(renderer.renderTopTenTable())
    }

    private fun writeTxt() {
        if (File(filesOutput.moduleReportTxt).exists()) {
            File(filesOutput.moduleReportTxt).delete()
        }
        File(filesOutput.moduleReportTxt).writeText(renderer.renderModulesText())
        if (File(filesOutput.moduleReportTxt).exists()) {
            println("${filesOutput.moduleReportTxt} created")
        }
    }

    private fun writeCsv() {
        if (File(filesOutput.moduleReportCsv).exists()) {
            File(filesOutput.moduleReportCsv).delete()
        }
        File(filesOutput.moduleReportCsv).writeText(renderer.renderCsv())
        if (File(filesOutput.moduleReportCsv).exists()) {
            println("${filesOutput.moduleReportCsv} created")
        }
    }
}
