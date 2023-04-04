plugins {
    id("kotlin.library")
    id("koin.core")
}

group = "com.booboot.vndbandroid.core.network.ktor"

dependencies {
    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.logging)
    implementation(libs.logback)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(projects.core.test.kotlin)
}
