package com.nikolam.feature_books.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<BookDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(bookDataModel: BookDataModel)

    @Query("SELECT * FROM books WHERE root_file = ")
    fun getBookByRootFile()
}