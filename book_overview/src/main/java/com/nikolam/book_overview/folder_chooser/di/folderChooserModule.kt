package com.nikolam.book_overview.folder_chooser.di

import android.content.Context
import android.content.SharedPreferences
import com.nikolam.book_overview.BookManager
import com.nikolam.book_overview.folder_chooser.data.StorageDirFinder
import com.nikolam.book_overview.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.book_overview.misc.BOOK_COLLECTION
import com.nikolam.book_overview.misc.BOOK_SINGLE
import org.koin.android.experimental.dsl.viewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val APPLICATION_ID = "com.nikolam.audiobookmate"

val storageModule = module {
    single{provideSharedPreferences(get())}
    single{StorageDirFinder(get())}
    single{BookManager(get())}
}

fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("${APPLICATION_ID}_preferences", Context.MODE_PRIVATE)
}

val viewModelModule : Module = module {
    viewModel{FolderChooserViewModel(get(), get())}
}


