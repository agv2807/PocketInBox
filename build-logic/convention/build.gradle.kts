plugins {
    `kotlin-dsl`
}

group = "com.vazdautsan.pocketinbox.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.pocketinbox.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("kmpLibrary") {
            id = libs.plugins.pocketinbox.kmp.library.get().pluginId
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("cmpLibrary") {
            id = libs.plugins.pocketinbox.cmp.library.get().pluginId
            implementationClass = "CmpLibraryConventionPlugin"
        }
        register("cmpFeature") {
            id = libs.plugins.pocketinbox.cmp.feature.get().pluginId
            implementationClass = "CmpFeatureConventionPlugin"
        }
        register("room") {
            id = libs.plugins.pocketinbox.room.get().pluginId
            implementationClass = "RoomConventionPlugin"
        }
        register("lint") {
            id = libs.plugins.pocketinbox.lint.get().pluginId
            implementationClass = "LintConventionPlugin"
        }
    }
}
