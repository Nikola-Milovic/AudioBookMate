package com.nikolam.audiobookmate.di

import com.nikolam.book_overview.FolderManager
import com.nikolam.book_overview.folder_chooser.di.provideSharedPreferences
import org.koin.dsl.module

val commonModule = module{
    single{ FolderManager(get()) }
    single{ provideSharedPreferences(get()) }
}