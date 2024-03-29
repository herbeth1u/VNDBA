plugins {
    id("android.application")
    id("android.application.compose")
    id("koin.android")
}

android {
    namespace = "com.booboot.vndbandroid.app"

    defaultConfig {
        applicationId = "com.booboot.vndbandroid"
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.feature.vnlist)
    implementation(projects.feature.search)
    implementation(projects.feature.explore)

    implementation(libs.bundles.compose.base)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.splashscreen)

    testImplementation(projects.core.test.android)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}