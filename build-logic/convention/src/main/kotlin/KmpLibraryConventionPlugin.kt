import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.vazdautsan.pocketinbox.buildlogic.ProjectConfig
import com.vazdautsan.pocketinbox.buildlogic.derivedNamespace
import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import com.vazdautsan.pocketinbox.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Kotlin Multiplatform library with Android and iOS targets.
 * Namespace is derived from the module path, see [derivedNamespace].
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-multiplatform"))
        pluginManager.apply(libs.pluginId("android-kotlin-multiplatform-library"))

        extensions.configure<KotlinMultiplatformExtension> {
            extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                namespace = derivedNamespace
                compileSdk = libs.version("android-compileSdk").toInt()
                minSdk = libs.version("android-minSdk").toInt()
                compilerOptions {
                    jvmTarget.set(ProjectConfig.jvmTarget)
                }
            }

            iosArm64()
            iosSimulatorArm64()
        }
    }
}
