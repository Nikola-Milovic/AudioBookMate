package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "chapters")
data class ChapterDataModel(
    @PrimaryKey(autoGenerate = true) val cid: Int,
    @ColumnInfo(name = "book_id") val bookId: Int,
    @ColumnInfo(name = "root_file") val root : File
)

internal fun ChapterDataModel.toDomainModel() {}