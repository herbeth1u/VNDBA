plugins {
    id("kotlin.library")
    id("koin.core")
}

group = "com.booboot.vndbandroid.core.network.vndb"

dependencies {
    implementation(projects.core.network.ktor)
}
