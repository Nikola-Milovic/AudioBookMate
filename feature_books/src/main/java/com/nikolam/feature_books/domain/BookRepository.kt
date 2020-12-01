package com.nikolam.feature_books.domain

import com.nikolam.feature_books.domain.model.BookDomainModel
import com.nikolam.feature_books.domain.model.ChapterDomainModel
import java.io.File

interface BookRepository {
    fun getAllBooks() : List<BookDomainModel>
    fun getChapterByRootFile(file : File) : ChapterDomainModel?
}