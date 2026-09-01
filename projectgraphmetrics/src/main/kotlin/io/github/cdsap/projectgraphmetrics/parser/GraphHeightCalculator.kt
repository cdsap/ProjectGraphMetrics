package io.github.cdsap.projectgraphmetrics.parser

internal class GraphHeightCalculator(
    edges: List<Pair<String, String>> = emptyList()
) {
    private val nodes = mutableMapOf<String, Node>()

    init {
        edges.forEach { (from, to) -> addEdge(from, to) }
    }

    fun addEdge(from: String, to: String) {
        getOrCreate(from).dependsOn.add(getOrCreate(to))
    }

    fun heightOf(key: String): Int = nodes[key]?.height() ?: -1

    private fun getOrCreate(key: String): Node {
        return nodes.getOrPut(key) { Node(key) }
    }

    private class Node(val key: String) {
        val dependsOn = mutableSetOf<Node>()
        private var visited = false
        private var calculatedHeight = -1

        fun height(): Int {
            if (visited) return 0
            if (calculatedHeight == -1) {
                visited = true
                calculatedHeight = if (dependsOn.isEmpty()) 0 else (1 + dependsOn.maxOfOrNull { it.height() }!!)
                visited = false
            }
            return calculatedHeight
        }
    }
}
