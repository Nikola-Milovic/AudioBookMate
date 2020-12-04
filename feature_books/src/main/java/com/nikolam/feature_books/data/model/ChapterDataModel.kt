package com.nikolam.feature_books.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nikolam.feature_books.domain.model.ChapterDomainModel
import java.io.File
import java.util.*

@Entity(tableName = "chapters")
data class ChapterDataModel(
    @ColumnInfo(name = "book_id")
    val bookId: UUID,
    @ColumnInfo(name = "root_file")
    val file: File,
    @ColumnInfo(name = "marks")
    val markData: List<MarkData>,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "duration")
    val duration: Long,
    @PrimaryKey(autoGenerate = true)
    val cid: Long = 0L
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
    internal fun ChapterDataModel.toDomainModel() : ChapterDomainModel{
        return ChapterDomainModel(File("asds"), 50, "asd", "")
    }