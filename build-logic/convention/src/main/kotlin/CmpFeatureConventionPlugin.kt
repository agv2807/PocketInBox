import com.vazdautsan.pocketinbox.buildlogic.library
import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Feature module: [CmpLibraryConventionPlugin] plus MVI base, domain, design system,
 * Koin ViewModels and type-safe navigation routes.
 */
class CmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pocketinbox-cmp-library"))
        pluginManager.apply(libs.pluginId("kotlin-serialization"))

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(project(":core:common"))
                implementation(project(":core:domain"))
                implementation(project(":core:designsystem"))

                implementation(libs.library("koin-core"))
                implementation(libs.library("koin-compose-viewmodel"))
                implementation(libs.library("lifecycle-viewmodel-compose"))
                implementation(libs.library("lifecycle-runtime-compose"))
                implementation(libs.library("navigation-compose"))
                implementation(libs.library("kotlinx-serialization-core"))
            }
        }
    }
}
