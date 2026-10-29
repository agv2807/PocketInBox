plugins {
    alias(libs.plugins.pocketinbox.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            implementation(projects.core.database)
            implementation(libs.koin.core)
        }
    }
}
