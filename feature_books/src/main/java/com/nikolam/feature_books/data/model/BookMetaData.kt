package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikolam.feature_books.misc.BookType
import java.util.*

@Entity(tableName = "bookMetaData")
data class BookMetaData(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: UUID,
    @ColumnInfo(name = "type")
    val type: BookType,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "root")
    val root: String,
    @ColumnInfo(name = "addedAtMillis")
    val addedAtMillis: Long
) {

    init {
        require(name.isNotEmpty()) { "name must not be empty" }
        require(root.isNotEmpty(), { "root must not be empty" })
    }
}
