package io.github.cdsap.projectgraphmetrics.cli.view

import com.jakewharton.picnic.renderText
import io.github.cdsap.projectgraphmetrics.model.GraphMetric
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class GraphReportRendererTest {

    private val modules =
        mapOf(
            ":core" to GraphMetric(height = 1, betweennessCentrality = 0.0, indegree = 2, outdegree = 0),
            ":feature" to GraphMetric(height = 2, betweennessCentrality = 1.5, indegree = 1, outdegree = 1),
            ":app" to GraphMetric(height = 3, betweennessCentrality = 0.5, indegree = 0, outdegree = 2)
        )

    @Test
    fun renderCsvProducesSortedModuleRowsWithoutTouchingFilesystem() {
        val before = snapshotReportFiles()

        val csv = GraphReportRenderer(modules).renderCsv()

        assertEquals(
            """
            Module,Indegree,Outdegree,BetweennessCentrality,Height
            :app,0,2,0.5,3
            :core,2,0,0.0,1
            :feature,1,1,1.5,2
            """.trimIndent(),
            csv
        )
        assertReportFilesUnchanged(before)
    }

    @Test
    fun renderModulesTextIncludesAllModulesAndHeadersWithoutTouchingFilesystem() {
        val before = snapshotReportFiles()

        val text = GraphReportRenderer(modules).renderModulesText()

        assertTrue(text.contains("Module"))
        assertTrue(text.contains("Indegree"))
        assertTrue(text.contains("Outdegree"))
        assertTrue(text.contains("BetweennessCentrality"))
        assertTrue(text.contains("Height"))
        assertTrue(text.contains(":app"))
        assertTrue(text.contains(":core"))
        assertTrue(text.contains(":feature"))
        assertTrue(text.contains("0.5"))
        assertReportFilesUnchanged(before)
    }

    @Test
    fun renderTopTenTextIncludesTitleAndTopModulesWithoutTouchingFilesystem() {
        val before = snapshotReportFiles()

        val text = GraphReportRenderer(modules).renderTopTenText()

        assertTrue(text.contains("Top Ten Module Report"))
        assertTrue(text.contains("Indegree"))
        assertTrue(text.contains("Outdegree"))
        assertTrue(text.contains("BetweennessCentrality"))
        assertTrue(text.contains("Height"))
        assertTrue(text.contains(":core"))
        assertTrue(text.contains(":app"))
        assertTrue(text.contains(":feature"))
        assertReportFilesUnchanged(before)
    }

    @Test
    fun renderTopTenTableMatchesRenderTopTenText() {
        val renderer = GraphReportRenderer(modules)
        assertEquals(renderer.renderTopTenTable().renderText(), renderer.renderTopTenText())
    }

    private fun snapshotReportFiles(): Map<File, String?> {
        return reportFiles.associateWith { if (it.exists()) it.readText() else null }
    }

    private fun assertReportFilesUnchanged(before: Map<File, String?>) {
        before.forEach { (file, content) ->
            val after = if (file.exists()) file.readText() else null
            assertEquals("Unexpected filesystem change for ${file.name}", content, after)
        }
        reportFiles.forEach { file ->
            if (before[file] == null) {
                assertFalse(file.exists())
            }
        }
    }

    companion object {
        private val reportFiles =
            listOf(
                File("modules_report.csv"),
                File("modules_report.txt"),
                File("top_ten_module_report.txt")
            )
    }
}
