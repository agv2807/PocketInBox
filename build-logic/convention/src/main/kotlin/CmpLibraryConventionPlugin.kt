import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.library
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** [KmpLibraryConventionPlugin] plus Compose Multiplatform and the base Compose libraries. */
class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pocketinbox-kmp-library"))
        pluginManager.apply(libs.pluginId("compose-multiplatform"))
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.library("compose-runtime"))
                implementation(libs.library("compose-foundation"))
                implementation(libs.library("compose-ui"))
                implementation(libs.library("compose-material3"))
                implementation(libs.library("compose-components-resources"))
                implementation(libs.library("compose-ui-tooling-preview"))
            }
        }
    }
}
