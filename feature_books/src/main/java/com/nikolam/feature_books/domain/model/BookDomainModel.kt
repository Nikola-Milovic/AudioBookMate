package com.nikolam.feature_books.domain.model

import com.nikolam.feature_books.misc.BookType

data class BookDomainModel(val bid : Int,
                           val root : String,
                           val type: BookType,
                           val content : List<ChapterDomainModel>
)