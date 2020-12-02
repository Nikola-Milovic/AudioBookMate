import io.gitlab.arturbosch.detekt.detekt

// all projects = root project + sub projects in li
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
    plugins.apply(GradlePluginId.GRADLE_VERSION_PLUGIN)

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

subprojects {
    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    }

    apply(plugin = GradlePluginId.DETEKT)

    detekt {
        config = files("${project.rootDir}/detekt.yml")
        parallel = true
    }
    afterEvaluate {
        configureAndroid()
    }
}

fun Project.configureAndroid() {
    (project.extensions.findByName("android") as? com.android.build.gradle.BaseExtension)?.run {
        packagingOptions {
            exclude("META-INF/metadata.jvm.kotlin_module")
            exclude("META-INF/metadata.kotlin_module")
        }

        sourceSets {
            map { it.java.srcDir("src/${it.name}/kotlin") }
        }
    }
}

task("staticCheck") {
    description =
            """Mimics all static checks that run on CI.
        Note that this task is intended to run locally (not on CI), because on CI we prefer to have parallel execution
        and separate reports for each check (multiple statuses eg. on github PR page).
        """.trimMargin()

    group = "verification"
    afterEvaluate {
        // Filter modules with "lintDebug" task (non-Android modules do not have lintDebug task)
        val lintTasks = subprojects.mapNotNull { "${it.name}:lintDebug" }

        // Get modules with "testDebugUnitTest" task (app module does not have it)
        val testTasks = subprojects.mapNotNull { "${it.name}:testDebugUnitTest" }
                .filter { it != "app:testDebugUnitTest" }

        // All task dependencies
        val taskDependencies =
                mutableListOf("app:assembleAndroidTest", "ktlintCheck", "detekt").also {
                    it.addAll(lintTasks)
                    it.addAll(testTasks)
                }

        // By defining Gradle dependency all dependent tasks will run before this "empty" task
        dependsOn(taskDependencies)
    }
}
