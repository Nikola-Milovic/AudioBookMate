private object TestLibraryVersion {
    const val JUNIT = "4.13"
    const val KLUENT = "1.60"
    const val TEST_RUNNER = "1.0.2"
    const val ESPRESSO = "3.1.0"
    const val MOCKITO = "3.6.28"
    const val MOCKITO_KOTLIN = "2.2.0"
    const val ANDROID_X_TEST = "2.1.0"
    const val FRAGMENT_VERSION = "1.2.5"
    const val KAKAO = "2.3.4"
    const val DATABINDING = "3.5.3"
    const val NAVIGATION = "2.3.0"
    const val HAMCREST = "2.2"
}

object TestLibraryDependency {
    const val JUNIT = "junit:junit:${TestLibraryVersion.JUNIT}"
    const val KLUENT_ANDROID = "org.amshove.kluent:kluent-android:${TestLibraryVersion.KLUENT}"
    const val KLUENT = "org.amshove.kluent:kluent:${TestLibraryVersion.KLUENT}"
    const val TEST_RUNNER = "com.android.support.test:runner:${TestLibraryVersion.TEST_RUNNER}"
    const val ESPRESSO_CORE =
            "com.android.support.test.espresso:espresso-core:${TestLibraryVersion.ESPRESSO}"
    const val MOCKITO_INLINE = "org.mockito:mockito-inline:${TestLibraryVersion.MOCKITO}"
    const val MOCKITO_ANDROID = "org.mockito:mockito-android:${TestLibraryVersion.MOCKITO}"
    const val MOCKITO_KOTLIN =
            "com.nhaarman.mockitokotlin2:mockito-kotlin:${TestLibraryVersion.MOCKITO_KOTLIN}"
    const val COROUTINES_TEST =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${CoreVersion.COROUTINES_ANDROID}"
    const val ANDROID_X_CORE_TESTING =
            "android.arch.core:core-testing:${TestLibraryVersion.ANDROID_X_TEST}"

    const val FRAGMENT_TESTING =
            "androidx.fragment:fragment-testing:${TestLibraryVersion.FRAGMENT_VERSION}"

    const val DATABINDING_COMPILER = "androidx.databinding:databinding-compiler:${TestLibraryVersion.DATABINDING}"
    const val KAKAO = "com.agoda.kakao:kakao:${TestLibraryVersion.KAKAO}"
    const val ESSPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${TestLibraryVersion.ESPRESSO}"

    const val NAVIGATION = "androidx.navigation:navigation-testing:${TestLibraryVersion.NAVIGATION}"
    const val HAMCREST = "org.hamcrest:hamcrest:${TestLibraryVersion.HAMCREST}"
}
