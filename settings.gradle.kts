rootProject.buildFileName = "build.gradle.kts"

rootProject.name = ("audiobookmate")

include(
    ":app",
    ":feature_player",
    ":feature_folders",
    ":feature_books",
    ":common"
)
