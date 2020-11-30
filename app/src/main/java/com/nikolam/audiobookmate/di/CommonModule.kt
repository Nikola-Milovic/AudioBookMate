package com.nikolam.audiobookmate.di

import android.content.Context
import android.content.SharedPreferences
import com.nikolam.audiobookmate.BuildConfig
import com.nikolam.feature_folders.FolderManager
import org.koin.dsl.module

val commonModule = module {
    single { FolderManager(get()) }
    single { provideSharedPreferences(get()) }
}

fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)
}
