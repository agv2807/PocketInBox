import androidx.room.gradle.RoomExtension
import com.vazdautsan.pocketinbox.buildlogic.library
import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Room on top of [KmpLibraryConventionPlugin]: KSP for every target, bundled SQLite driver,
 * exported schemas in `<module>/schemas`.
 */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pocketinbox-kmp-library"))
        pluginManager.apply(libs.pluginId("ksp"))
        pluginManager.apply(libs.pluginId("room"))

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            // Room's database constructor is an `expect object`
            compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

            sourceSets.commonMain.dependencies {
                api(libs.library("room-runtime"))
                implementation(libs.library("sqlite-bundled"))
            }
        }

        dependencies {
            listOf("kspAndroid", "kspIosArm64", "kspIosSimulatorArm64").forEach { configuration ->
                add(configuration, libs.library("room-compiler"))
            }
        }
    }
}
