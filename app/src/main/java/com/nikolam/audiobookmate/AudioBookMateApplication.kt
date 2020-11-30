package com.nikolam.audiobookmate

import android.app.Application
import com.nikolam.audiobookmate.di.commonModule
import com.nikolam.audiobookmate.di.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

class AudioBookMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@AudioBookMateApplication)
        }

        loadKoinModules(listOf(navigationModule, commonModule))
        // This will initialise Timber
        // This will initialise Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
