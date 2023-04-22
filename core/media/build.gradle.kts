plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.booboot.vndbandroid.core.media"
}

dependencies {
    implementation(libs.coil)
    implementation(libs.zoomable)

    implementation(libs.bundles.compose.base)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
