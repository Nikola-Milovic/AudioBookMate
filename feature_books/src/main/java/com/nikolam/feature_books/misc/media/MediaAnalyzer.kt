package com.nikolam.feature_books.misc.media

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFprobe
import com.nikolam.feature_books.data.model.MarkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import timber.log.Timber
import java.io.File
import java.lang.Exception
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


@ExperimentalTime
class MediaAnalyzer() {

    private val json = Json{ignoreUnknownKeys= true}

    suspend fun analyze(file: File): Result {
        Timber.d("analyze $file")

        val elements = ffProbeCommand(file)
        val result = ffprobe(*elements.toTypedArray())
        if (result.code != 0) {
            Timber.e("Unable to parse $file, ${result.message}")
            return Result.Failure
        }
        Timber.d(result.message)

        val parsed = try {
            json.decodeFromString(MetaDataScanResult.serializer(), result.message)
        } catch (e: Exception) {
            Timber.e(e, "Unable to parse $file")
            return Result.Failure
        }

        val duration = parsed.format?.duration
        return if (duration != null && duration > 0) {
            Result.Success(
                duration = duration.seconds.toLongMilliseconds(),
                chapterName = parsed.findTag(TagType.Title) ?: chapterNameFallback(file),
                author = parsed.findTag(TagType.Artist),
                bookName = parsed.findTag(TagType.Album),
                chapters = parsed.chapters.mapIndexed { index, metaDataChapter ->
                    MarkData(
                        startMs = metaDataChapter.start.toLongMilliseconds(),
                        name = metaDataChapter.tags?.find(TagType.Title) ?: (index + 1).toString()
                    )
                }
            )
        } else {
            Timber.e("Unable to parse $file")
            Result.Failure
        }
    }

    sealed class Result {
        data class Success(
            val duration: Long,
            val chapterName: String,
            val author: String?,
            val bookName: String?,
            val chapters: List<MarkData>
        ) : Result() {
            init {
                require(duration > 0)
            }
        }

        object Failure : Result()
    }
}

private fun ffProbeCommand(file: File): List<String> {
    return listOf(
        "-i", file.absolutePath,
        "-print_format", "json=c=1",
        "-show_chapters",
        "-loglevel", "quiet",
        "-show_entries", "format=duration",
        "-show_entries", "format_tags=artist,title,album",
        "-show_entries", "stream_tags=artist,title,album",
        "-select_streams", "a" // only select the audio stream
    )
}

private fun chapterNameFallback(file: File): String {
    val name = file.name ?: "Chapter"
    return name.substringBeforeLast(".")
        .trim()
        .takeUnless { it.isEmpty() }
        ?: name
}

suspend fun ffprobe(vararg command: String): FfmpegCommandResult = withDispatcherAndLock {
    val code = FFprobe.execute(command)
    val message = Config.getLastCommandOutput()
    FfmpegCommandResult(message, code)
}

private suspend inline fun <T> withDispatcherAndLock(crossinline action: () -> T): T {
    return withContext(Dispatchers.Default) {
        mutex.withLock {
            action()
        }
    }
}

private val mutex = Mutex()

data class FfmpegCommandResult(
    val message: String,
    val code: Int
)