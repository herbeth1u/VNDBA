pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VNDBA"
include(":app")

include(":core:designsystem")
include(":core:network:ktor")
include(":core:network:vndb")
include(":core:storage:preferences")
include(":core:test:kotlin")
include(":core:test:android")
include(":core:media")

include(":feature:vnlist")
include(":feature:search")
include(":feature:explore")
