package io.github.cdsap.projectgraphmetrics.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import io.github.cdsap.projectgraphmetrics.cli.view.GraphViewWriter
import io.github.cdsap.projectgraphmetrics.parser.GraphParser

fun main(args: Array<String>) {
    DependenciesReport().main(args)
}

class DependenciesReport : CliktCommand() {
    private val file by option().file().required()

    override fun run() {
        val modules = GraphParser(file.path).getIndicatorsByModule()
        GraphViewWriter(modules).generate()
    }
}
