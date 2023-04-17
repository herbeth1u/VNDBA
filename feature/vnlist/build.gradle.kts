plugins {
    id("android.library")
    id("android.library.compose")
    id("koin.android")
}

android {
    namespace = "com.booboot.vndbandroid.feature.vnlist"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)
}