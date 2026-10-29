plugins {
    alias(libs.plugins.pocketinbox.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.lifecycle.viewmodel)
            api(libs.kotlinx.coroutines.core)
            implementation(libs.lifecycle.runtime.compose)
        }
    }
}
