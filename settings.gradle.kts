pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlinx")
    }
}

rootProject.buildFileName = "build.gradle.kts"

rootProject.name = ("audiobookmate")

include(
    ":app",
    ":feature_player",
    ":feature_folders",
    ":feature_books",
    ":library_test_utils",
    ":common"
)
