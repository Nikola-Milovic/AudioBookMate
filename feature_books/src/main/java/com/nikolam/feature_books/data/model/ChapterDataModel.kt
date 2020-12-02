package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "chapters")
data class ChapterDataModel(
    @PrimaryKey(autoGenerate = true)
    val cid: Int,
    @ColumnInfo(name = "book_id")
    val bookId: Int,
    @ColumnInfo(name = "root_file")
    val root: File,
    @ColumnInfo(name = "marks")
    val markData: List<MarkData>,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "duration")
    val duration: Long,
) {
    @Ignore
    val chapterMarks: List<ChapterMark>

    init {
        chapterMarks = if (markData.isEmpty()) {
            listOf(ChapterMark(name, 0L, duration))
        } else {
            val sorted = markData.sorted()
            sorted.mapIndexed { index, (startMs, name) ->
                val isFirst = index == 0
                val isLast = index == sorted.size - 1
                val start = if (isFirst) 0L else startMs
                val end = if (isLast) duration else sorted[index + 1].startMs - 1
                ChapterMark(name = name, startMs = start, endMs = end)
            }
        }
    }
}
    internal fun ChapterDataModel.toDomainModel() {}