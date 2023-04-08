plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.booboot.vndbandroid.feature.explore"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)
}