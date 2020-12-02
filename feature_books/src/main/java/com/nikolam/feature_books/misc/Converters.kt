package com.nikolam.feature_books.misc

import androidx.room.TypeConverter
import com.nikolam.feature_books.data.model.MarkData
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import java.io.File
import java.util.*

class Converters {

    private val markDataListSerializer = ListSerializer(MarkData.serializer())

    @TypeConverter
    fun fromMarks(data: List<MarkData>): String = Json.encodeToString(markDataListSerializer, data)

    @TypeConverter
    fun toMarks(string: String): List<MarkData> = Json.decodeFromString(markDataListSerializer, string)

    @TypeConverter
    fun fromFile(file: File): String = file.absolutePath

    @TypeConverter
    fun toFile(path: String) = File(path)

    @TypeConverter
    fun fromBookType(type: BookType): String = type.name

    @TypeConverter
    fun toBookType(name: String): BookType = BookType.valueOf(name)

    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(string: String): UUID = UUID.fromString(string)
}