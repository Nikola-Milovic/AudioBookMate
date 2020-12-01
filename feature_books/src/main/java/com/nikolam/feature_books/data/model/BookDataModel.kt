package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "books")
data class BookDataModel(
    @PrimaryKey(autoGenerate = true) val bid: Int,
    @ColumnInfo(name = "book_author") val bookAuthor: String?,
    @ColumnInfo(name = "book_title") val bookTitle: String?,
    @ColumnInfo(name = "root_file") val root : File?
)

internal fun BookDataModel.toDomainModel() {}