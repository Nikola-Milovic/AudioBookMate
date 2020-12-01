package com.nikolam.feature_books.data.books

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikolam.feature_books.data.model.BookDataModel
import com.nikolam.feature_books.data.model.ChapterDataModel
import java.io.File

@Dao
interface ChapterDao {
    @Query("SELECT * FROM books WHERE root_file = :file")
    fun getChapterByRootFile(file : File): ChapterDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapter(chapterDataModel: ChapterDataModel)
}