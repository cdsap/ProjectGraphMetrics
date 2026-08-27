package io.github.cdsap.projectgraphmetrics.parser

import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.jgrapht.alg.scoring.BetweennessCentrality
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.jgrapht.nio.dot.DOTImporter
import java.io.File
import java.io.FileNotFoundException
import java.text.DecimalFormat

class GraphParser(private val fileGraph: String) {
    private val decimalFormat = DecimalFormat("#.##")
    private val result: SimpleDirectedGraph<String, DefaultEdge>
    private val betweennessCentrality: Map<String, Double>
    private val heightCalculator = HeightCalculator()

    init {
        checkFile()
        val importer = DOTImporter<String, DefaultEdge>().apply {
            setVertexFactory { it }
        }
        result = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        importer.importGraph(result, File(fileGraph))

        val edgesParsed = result.edgeSet().map {
            it.toString().removeSurrounding("(", ")").replace(".", "_").split(" : ")
                .let { parts -> parts[0] to parts[1] }
        }
        edgesParsed.forEach { (from, to) -> heightCalculator.addEdge(from, to) }
        betweennessCentrality = BetweennessCentrality(result).scores
    }

    private fun checkFile() {
        if (!File(fileGraph).exists()) {
            throw FileNotFoundException("$fileGraph not found")
        }
    }

    fun result() = result

    fun betweennessCentrality(module: String) =
        decimalFormat.format(betweennessCentrality[module] ?: 0.0).toDouble()

    fun inDegree(module: String) = try {
        result.inDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    fun outDegree(module: String) = try {
        result.outDegreeOf(module)
    } catch (e: IllegalArgumentException) {
        0
    }

    fun heightOf(key: String): Int = heightCalculator.heightOf(key)

    fun getIndicatorsByModule(): Map<String, GraphMetric> {
        return result().vertexSet().associateWith {
            GraphMetric(
                height = heightOf(it),
                indegree = inDegree(it),
                outdegree = outDegree(it),
                betweennessCentrality = betweennessCentrality(it)
            )
        }
    }
}
