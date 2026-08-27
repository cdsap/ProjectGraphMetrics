package io.github.cdsap.projectgraphmetrics.parser

import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.jgrapht.nio.dot.DOTImporter
import java.io.File
import java.io.FileNotFoundException

internal class DotGraphLoader {

    fun load(fileGraph: String): SimpleDirectedGraph<String, DefaultEdge> {
        checkFile(fileGraph)
        val importer = DOTImporter<String, DefaultEdge>().apply {
            setVertexFactory { it }
        }
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        importer.importGraph(graph, File(fileGraph))
        return graph
    }

    private fun checkFile(fileGraph: String) {
        if (!File(fileGraph).exists()) {
            throw FileNotFoundException("$fileGraph not found")
        }
    }
}
