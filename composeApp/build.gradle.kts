plugins {
    alias(libs.plugins.pocketinbox.cmp.library)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "com.vazdautsan.pocketinbox.composeapp")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.data)
            api(libs.koin.core)
        }
    }
}
