package io.github.cdsap.projectgraphmetrics.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GraphHeightCalculatorTest {

    @Test
    fun heightOfSimpleDependencyChain() {
        val calculator = GraphHeightCalculator(
            listOf("A" to "B", "B" to "C")
        )

        assertEquals(0, calculator.heightOf("C"))
        assertEquals(1, calculator.heightOf("B"))
        assertEquals(2, calculator.heightOf("A"))
    }

    @Test
    fun heightOfUnknownModuleIsMinusOne() {
        val calculator = GraphHeightCalculator(listOf("A" to "B"))

        assertEquals(-1, calculator.heightOf("missing"))
    }

    @Test
    fun cycleGuardReturnsFiniteHeight() {
        val calculator = GraphHeightCalculator()
        calculator.addEdge("A", "B")
        calculator.addEdge("B", "A")

        val heightA = calculator.heightOf("A")
        val heightB = calculator.heightOf("B")

        assertTrue(heightA >= 0)
        assertTrue(heightB >= 0)
        assertEquals(2, heightA)
        assertEquals(1, heightB)
    }

    @Test
    fun heightOfEmptyEdgeListIsMinusOne() {
        val calculator = GraphHeightCalculator(emptyList())

        assertEquals(-1, calculator.heightOf("A"))
    }
}
