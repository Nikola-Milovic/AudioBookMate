package com.nikolam.feature_books.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikolam.feature_books.data.books.BookDao
import com.nikolam.feature_books.data.books.ChapterDao
import com.nikolam.feature_books.data.model.BookDataModel
import com.nikolam.feature_books.data.model.ChapterDataModel

@Database(entities = [BookDataModel::class, ChapterDataModel::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
}