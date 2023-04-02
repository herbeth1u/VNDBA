plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.booboot.vndbandroid.core.test.android"
}

dependencies {
    api(project(":core:test:kotlin"))

    api(libs.bundles.mockk.android.test)
    api(libs.androidx.test.junit)
    api(libs.androidx.test.espresso)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.bundles.compose.test)
    api(libs.robolectric)
    debugApi(libs.androidx.compose.ui.testManifest)
}
