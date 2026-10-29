plugins {
    alias(libs.plugins.pocketinbox.android.application)
}

android {
    namespace = "com.vazdautsan.pocketinbox"

    defaultConfig {
        applicationId = "com.vazdautsan.pocketinbox"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
