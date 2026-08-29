package io.github.cdsap.projectgraphmetrics.cli

import com.github.ajalt.clikt.core.main
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class DependenciesReportTest {

    private val generatedReports =
        listOf(
            File("modules_report.csv"),
            File("modules_report.txt"),
            File("top_ten_module_report.txt")
        )
    private val originalReports = generatedReports.associateWith { if (it.exists()) it.readText() else null }

    @After
    fun restoreGeneratedReports() {
        originalReports.forEach { (file, content) ->
            if (content == null) {
                file.delete()
            } else {
                file.writeText(content)
            }
        }
    }

    @Test
    fun dependenciesReportGeneratesExistingOutputFiles() {
        val dotFile = Files.createTempFile("dependencies-report", ".dot").toFile()
        dotFile.writeText(
            """
            digraph G {
            ":app" -> ":feature"
            ":feature" -> ":core"
            }
            """.trimIndent()
        )

        try {
            DependenciesReport().main(arrayOf("--file", dotFile.path))

            assertTrue(File("modules_report.csv").exists())
            assertTrue(File("modules_report.txt").exists())
            assertTrue(File("top_ten_module_report.txt").exists())
            assertTrue(File("modules_report.csv").readText().contains(":app,0,1"))
        } finally {
            dotFile.delete()
        }
    }

    @Test
    fun dependenciesReportUsesLibraryFacadeInsteadOfParserImplementation() {
        val mainSource =
            listOf(
                Paths.get("src/main/kotlin/io/github/cdsap/projectgraphmetrics/cli/Main.kt").toFile(),
                Paths.get("cli/src/main/kotlin/io/github/cdsap/projectgraphmetrics/cli/Main.kt").toFile()
            ).first { it.exists() }.readText()

        assertTrue(mainSource.contains("import io.github.cdsap.projectgraphmetrics.ProjectGraphMetrics"))
        assertFalse(mainSource.contains("parser.GraphParser"))
        assertFalse(mainSource.contains("GraphParser("))
    }
}
