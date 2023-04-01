plugins {
    id("kotlin.library")
    id("koin.core")
}

group = "com.booboot.vndbandroid.core.network.vndb"

dependencies {
    implementation(project(":core:network:ktor"))
}
