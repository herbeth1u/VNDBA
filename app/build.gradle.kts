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
    implementation(project(":core:designsystem"))
    implementation(project(":core:network:vndb")) // TODO move to data module

    implementation(libs.bundles.compose.base)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)

    androidTestImplementation(project(":core:test:android"))
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // TODO move to data module
    implementation(libs.kotlinx.serialization.json)
}