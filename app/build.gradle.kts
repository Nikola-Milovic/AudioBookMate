plugins {
    id(GradlePluginId.ANDROID_APPLICATION)
    id(GradlePluginId.KOTLIN_ANDROID)
    id(GradlePluginId.KOTLIN_ANDROID_EXTENSIONS)
    id(GradlePluginId.KOTLIN_KAPT)
    id(GradlePluginId.SAFE_ARGS)
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId = AndroidConfig.ID
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
    }

    testOptions {
        animationsDisabled = true
    }
}
dependencies {
    api(LibraryDependency.ANDROID_LEGACY_SUPPORT)
    api(LibraryDependency.LIFECYCLE_EXTENSIONS)
    api(LibraryDependency.LIFECYCLE_VIEW_MODEL_KTX)

    api(LibraryDependency.TIMBER)
    api(LibraryDependency.NAVIGATION_FRAGMENT_KTX)
    api(LibraryDependency.NAVIGATION_UI_KTX)
    api(LibraryDependency.NAVIGATION_DYNAMIC_FEATURES)

    api(LibraryDependency.RECYCLER_VIEW)
    api(LibraryDependency.MATERIAL)
    api(LibraryDependency.FRAGMENT_KTX)

    api(LibraryDependency.SUPPORT_CONSTRAINT_LAYOUT)

    api(LibraryDependency.KOIN_ANDROID)
    api(LibraryDependency.KOIN_ANDROID_EXTENSION)
    api(LibraryDependency.KOIN_ANDROID_SCOPE)
    api(LibraryDependency.KOIN_ANDROID_VIEWMODEL)

    implementation(LibraryDependency.EASY_PERMISSION)

    implementation(project(":feature_player"))
    implementation(project(":feature_folders"))

    addTestDependencies()
}
