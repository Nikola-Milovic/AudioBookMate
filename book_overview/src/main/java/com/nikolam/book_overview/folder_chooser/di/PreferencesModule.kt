package com.nikolam.book_overview.folder_chooser.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.dsl.module

const val APPLICATION_ID = "com.nikolam.audiobookmate"

val preferencesModule = module {


    single{provideSharedPreferences(get())}


}

fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("${APPLICATION_ID}_preferences", Context.MODE_PRIVATE)
}

