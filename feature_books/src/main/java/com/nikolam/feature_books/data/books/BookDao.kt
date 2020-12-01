package com.nikolam.feature_books.data.books

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikolam.feature_books.data.model.BookDataModel
import java.io.File

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<BookDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(bookDataModel: BookDataModel)

    @Query("SELECT * FROM books WHERE root_file = :file")
    fun getBookByRootFile(file : File) : BookDataModel
}