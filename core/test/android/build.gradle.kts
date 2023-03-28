plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.showroomprive.core.test.android"
}

dependencies {
    api(project(":core:test:kotlin"))
    api(libs.androidx.test.espresso)
    api(libs.androidx.test.junit)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.bundles.compose.test)
    api(libs.bundles.mockk.android.test)

    debugApi(libs.androidx.compose.ui.testManifest)
}
