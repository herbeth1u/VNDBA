plugins {
    id("kotlin.library")
    id("koin.core")
}

group = "com.booboot.vndbandroid.core.storage.preferences"

dependencies {
    implementation(libs.androidx.datastore.preferences.core)

    testImplementation(projects.core.test.kotlin)
}
