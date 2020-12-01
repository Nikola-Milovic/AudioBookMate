package com.nikolam.feature_books.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookDataModel::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}