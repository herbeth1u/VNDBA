plugins {
    id("android.application")
    id("android.application.compose")
    id("koin.android")
}

android {
    namespace = "com.booboot.vndbandroid"

    defaultConfig {
        applicationId = "com.booboot.vndbandroid"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.bundles.compose.base)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)

    testImplementation(projects.core.test.android)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}