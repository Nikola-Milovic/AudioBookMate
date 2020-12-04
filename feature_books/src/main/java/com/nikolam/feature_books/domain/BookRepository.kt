package com.nikolam.feature_books.domain

import com.nikolam.feature_books.data.model.BookDataModel
import com.nikolam.feature_books.data.model.ChapterDataModel
import com.nikolam.feature_books.domain.model.BookDomainModel
import com.nikolam.feature_books.domain.model.ChapterDomainModel
import java.io.File
import java.util.*

interface BookRepository {
    suspend fun getAllBooks() : List<BookDomainModel>
    suspend fun getChapterByRootFile(file : File) : ChapterDataModel?
    suspend fun deleteBook(bookId : UUID)
    suspend fun addBook(book : BookDomainModel)
}