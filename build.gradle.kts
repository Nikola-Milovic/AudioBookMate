// all projects = root project + sub projects
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.fabric.io/public")
        maven("https://plugins.gradle.org/m2/")
    }

    plugins.apply(GradlePluginId.DETEKT)
    plugins.apply(GradlePluginId.KTLINT)
    plugins.apply(GradlePluginId.SPOTLESS)
    plugins.apply(GradlePluginId.GRADLE_VERSION_PLUGIN)


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
