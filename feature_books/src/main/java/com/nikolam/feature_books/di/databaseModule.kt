package com.nikolam.feature_books.di

import android.content.Context
import androidx.room.Room
import com.nikolam.feature_books.data.BookDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(get())}
}

private fun provideDatabase(applicationContext : Context) : BookDatabase {
    return Room.databaseBuilder(
        applicationContext,
        BookDatabase::class.java, "book-db"
    ).build()
}
