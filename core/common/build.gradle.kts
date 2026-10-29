plugins {
    alias(libs.plugins.pocketinbox.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.lifecycle.viewmodel)
            api(libs.kotlinx.coroutines.core)
        }
    }
}
