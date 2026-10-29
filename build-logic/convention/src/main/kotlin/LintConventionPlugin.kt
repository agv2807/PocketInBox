import com.vazdautsan.pocketinbox.buildlogic.library
import com.vazdautsan.pocketinbox.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.process.CommandLineArgumentProvider

/**
 * ktlint and detekt CLIs for the root project. Files are passed as
 * `-PlintFiles=path1,path2` (relative to the repo root); without the property the whole
 * project is checked. Tasks: `ktlintFiles`, `ktlintFormatFiles`, `detektFiles`.
 */
class LintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        check(this == rootProject) { "pocketinbox.lint is applied to the root project only" }

        val ktlint = configurations.create("ktlint") {
            isCanBeConsumed = false
            attributes {
                // ktlint-cli publishes a self-contained (shadowed) variant next to the regular one
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.SHADOWED))
            }
        }
        val detekt = configurations.create("detekt") {
            isCanBeConsumed = false
        }
        dependencies.add(ktlint.name, libs.library("ktlint-cli"))
        dependencies.add(detekt.name, libs.library("detekt-cli"))

        val lintFiles = providers.gradleProperty("lintFiles")
            .map { value -> value.split(',').map(String::trim).filter(String::isNotEmpty) }
            .orElse(emptyList())
        val detektConfig = layout.projectDirectory.file("config/detekt/detekt.yml").asFile.path

        fun registerKtlint(name: String, format: Boolean) = tasks.register<JavaExec>(name) {
            group = "verification"
            description = "Runs ktlint${if (format) " with autocorrect" else ""} on -PlintFiles or the whole project"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            argumentProviders.add(
                CommandLineArgumentProvider {
                    buildList {
                        add("--relative")
                        if (format) add("--format")
                        addAll(lintFiles.get().ifEmpty { listOf("**/*.kt", "**/*.kts", "!**/build/**") })
                    }
                },
            )
        }
        registerKtlint("ktlintFiles", format = false)
        registerKtlint("ktlintFormatFiles", format = true)

        tasks.register<JavaExec>("detektFiles") {
            group = "verification"
            description = "Runs detekt on -PlintFiles or the whole project"
            classpath = detekt
            mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
            argumentProviders.add(
                CommandLineArgumentProvider {
                    val files = lintFiles.get()
                    listOf(
                        "--input",
                        if (files.isEmpty()) "." else files.joinToString(","),
                        "--excludes",
                        "**/build/**",
                        "--config",
                        detektConfig,
                        "--build-upon-default-config",
                    )
                },
            )
        }
    }
}
