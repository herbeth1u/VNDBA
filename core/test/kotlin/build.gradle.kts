plugins {
    id("kotlin.library")
    id("koin.core")
}

group = "com.booboot.vndbandroid.core.testing"

dependencies {
    api(kotlin("test"))

    api(libs.kotlinx.coroutines.test)

    api(libs.koin.test)
    api(libs.koin.test.junit)
    api(libs.bundles.mockk.test)
}
