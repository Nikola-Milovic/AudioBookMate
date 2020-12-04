package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikolam.feature_books.misc.BookType
import java.io.File
import java.util.*

@Entity(tableName = "books")
data class BookDataModel(
    @PrimaryKey() val bid: UUID,
    @ColumnInfo(name = "book_author") val bookAuthor: String?,
    @ColumnInfo(name = "book_title") val bookTitle: String?,
    @ColumnInfo(name = "root_file") val root: File?,
    @ColumnInfo(name = "book_type") val bookType: BookType?,
)

internal fun BookDataModel.toDomainModel() {}