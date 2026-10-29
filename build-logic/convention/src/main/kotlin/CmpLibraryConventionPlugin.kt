import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.vazdautsan.pocketinbox.buildlogic.derivedNamespace
import com.vazdautsan.pocketinbox.buildlogic.library
import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * [KmpLibraryConventionPlugin] plus Compose Multiplatform and the base Compose libraries.
 * Compose resources get a module-specific `Res` class in `<namespace>.resources`.
 */
class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pocketinbox-kmp-library"))
        pluginManager.apply(libs.pluginId("compose-multiplatform"))
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        extensions.configure<KotlinMultiplatformExtension> {
            extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                // Packs composeResources into the Android artifact
                androidResources {
                    enable = true
                }
            }

            sourceSets.commonMain.dependencies {
                implementation(libs.library("compose-runtime"))
                implementation(libs.library("compose-foundation"))
                implementation(libs.library("compose-ui"))
                implementation(libs.library("compose-material3"))
                implementation(libs.library("compose-components-resources"))
                implementation(libs.library("compose-ui-tooling-preview"))
            }
        }

        extensions.configure<ComposeExtension> {
            extensions.configure<ResourcesExtension> {
                packageOfResClass = "$derivedNamespace.resources"
            }
        }
    }
}
