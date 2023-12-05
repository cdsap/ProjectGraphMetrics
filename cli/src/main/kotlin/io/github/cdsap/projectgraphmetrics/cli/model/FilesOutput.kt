package io.github.cdsap.projectgraphmetrics.cli.model

data class FilesOutput(
    val moduleReportTxt: String,
    val moduleReportCsv: String,
    val topTenModuleReportTxt: String
)
