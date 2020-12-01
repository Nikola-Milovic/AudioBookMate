package com.nikolam.feature_books.domain

interface BookRepository {
    fun getAllBooks() : List<BookDomainModel>
}