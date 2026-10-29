import com.android.build.api.dsl.ApplicationExtension
import com.vazdautsan.pocketinbox.buildlogic.ProjectConfig
import com.vazdautsan.pocketinbox.buildlogic.libs
import com.vazdautsan.pocketinbox.buildlogic.pluginId
import com.vazdautsan.pocketinbox.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android application with built-in Kotlin and Compose. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        extensions.configure<ApplicationExtension> {
            compileSdk {
                version = release(libs.version("android-compileSdk").toInt())
            }
            defaultConfig {
                minSdk = libs.version("android-minSdk").toInt()
                targetSdk = libs.version("android-targetSdk").toInt()
            }
            compileOptions {
                sourceCompatibility = ProjectConfig.javaVersion
                targetCompatibility = ProjectConfig.javaVersion
            }
            buildFeatures {
                compose = true
            }
        }
    }
}
