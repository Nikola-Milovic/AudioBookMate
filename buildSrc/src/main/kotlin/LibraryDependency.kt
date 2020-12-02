@file:Suppress("detekt.StringLiteralDuplication")

private object LibraryVersion {
    const val TIMBER = "4.7.1"
    const val PLAY_CORE = "1.7.3" //164
    const val APP_COMPACT = "1.1.0"
    const val RECYCLER_VIEW = "1.2.0-alpha05"
    const val COORDINATOR_LAYOUT = "1.1.0"
    // 1.1.x version is required in order to support the dark theme functionality in
    // Android Q(adds Theme.MaterialComponents.DayNight)
    const val MATERIAL = "1.1.0-alpha09"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val CORE_KTX = "1.1.0"
    const val FRAGMENT_KTX = "1.2.5" //1.1.0
    const val LIFECYCLE = "2.2.0"
    const val LIFECYCLE_VIEW_MODEL_KTX = "2.2.0"
    const val ANDROID_LEGACY_SUPPORT = "1.0.0"
    //Glide
    const val GLIDE = "4.11.0"
    //Koin
    const val KOIN = "2.2.0"
    const val EASY_PERMISSION = "3.0.0"
    const val FLOATING_ACTION_BUTTON = "1.10.1"
    const val ROOM = "2.2.5"

    const val FFMPEG = "4.3.2"

    const val SERIALIZATION_RUNTIME = "1.0.0-RC2"
}

object LibraryDependency {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${CoreVersion.KOTLIN}"
    // Required by Android dynamic feature modules and SafeArgs
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${CoreVersion.KOTLIN}"
    const val KOTLIN_STD = "org.jetbrains.kotlin:kotlin-stdlib:${CoreVersion.KOTLIN}"

    const val TIMBER = "com.jakewharton.timber:timber:${LibraryVersion.TIMBER}"
    const val PLAY_CORE = "com.google.android.play:core:${LibraryVersion.PLAY_CORE}"
    const val COROUTINES_ANDROID =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${CoreVersion.COROUTINES_ANDROID}"
    const val CORE_KTX = "androidx.core:core-ktx:${LibraryVersion.CORE_KTX}"

    //UI
    const val SUPPORT_CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${LibraryVersion.CONSTRAINT_LAYOUT}"
    const val APP_COMPACT = "androidx.appcompat:appcompat:${LibraryVersion.APP_COMPACT}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${LibraryVersion.RECYCLER_VIEW}"
    const val MATERIAL = "com.google.android.material:material:${LibraryVersion.MATERIAL}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${LibraryVersion.FRAGMENT_KTX}"
    const val FLOATING_ACTION_BUTTON = "com.getbase:floatingactionbutton:${LibraryVersion.FLOATING_ACTION_BUTTON}"

    //Glide
    const val GLIDE = "com.github.bumptech.glide:glide:${LibraryVersion.GLIDE}"
    const val GLIDE_AP = "com.github.bumptech.glide:compiler:${LibraryVersion.GLIDE}"

    //LifeCycle
    const val LIFECYCLE_EXTENSIONS =
            "androidx.lifecycle:lifecycle-extensions:${LibraryVersion.LIFECYCLE}"
    const val LIFECYCLE_VIEW_MODEL_KTX =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibraryVersion.LIFECYCLE_VIEW_MODEL_KTX}"
    const val ANDROID_LEGACY_SUPPORT =
            "androidx.legacy:legacy-support-v4:${LibraryVersion.ANDROID_LEGACY_SUPPORT}"

    const val NAVIGATION_FRAGMENT_KTX =
            "androidx.navigation:navigation-fragment-ktx:${CoreVersion.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${CoreVersion.NAVIGATION}"
    const val NAVIGATION_DYNAMIC_FEATURES =
            "androidx.navigation:navigation-dynamic-features-fragment:${CoreVersion.NAVIGATION}"

    //Koin
    const val KOIN_ANDROID = "org.koin:koin-android:${LibraryVersion.KOIN}"
    const val KOIN_ANDROID_SCOPE = "org.koin:koin-android-scope:${LibraryVersion.KOIN}"
    const val KOIN_ANDROID_VIEWMODEL = "org.koin:koin-android-viewmodel:${LibraryVersion.KOIN}"
    const val KOIN_ANDROID_EXTENSION = "org.koin:koin-android-ext:${LibraryVersion.KOIN}"

    const val EASY_PERMISSION = "pub.devrel:easypermissions:${LibraryVersion.EASY_PERMISSION}"

    //Room
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${LibraryVersion.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${LibraryVersion.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${LibraryVersion.ROOM}"

    const val FFMPEG = "com.arthenica:mobile-ffmpeg-audio:${LibraryVersion.FFMPEG}"
    const val SERIALIZATION_JSON = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibraryVersion.SERIALIZATION_RUNTIME}"
}
