package com.nikolam.feature_books.data

import android.content.SharedPreferences
import com.nikolam.common.BOOK_COLLECTION
import com.nikolam.common.BOOK_SINGLE
import com.nikolam.common.FileRecognition
import com.nikolam.common.NaturalOrderComparator
import com.nikolam.common.extensions.listFilesSafely
import com.nikolam.feature_books.data.model.*
import com.nikolam.feature_books.domain.BookRepository
import com.nikolam.feature_books.domain.model.BookDomainModel
import com.nikolam.feature_books.domain.model.ChapterDomainModel
import com.nikolam.feature_books.misc.BookType
import com.nikolam.feature_books.misc.media.MediaAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.time.ExperimentalTime

@ExperimentalTime
class BookScanner(
    private val sharedPreferences: SharedPreferences,
    private val repository: BookRepository,
) {

    lateinit var singleBookFolderPref: Set<String>
    lateinit var collectionBookFolderPref: Set<String>

    lateinit var booksList: ArrayList<BookDomainModel>

    val mediaAnalyzer = MediaAnalyzer()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            singleBookFolderPref =
                HashSet(sharedPreferences.getStringSet(BOOK_SINGLE, mutableSetOf())
                    ?: mutableSetOf())
        }
        GlobalScope.launch(Dispatchers.IO) {
            collectionBookFolderPref =
                HashSet(sharedPreferences.getStringSet(BOOK_COLLECTION, mutableSetOf())
                    ?: mutableSetOf())
        }
    }

    private val singleBookFiles: List<File>
        get() = singleBookFolderPref
            .map(::File)
            .sortedWith(NaturalOrderComparator.fileComparator)

    private val collectionBookFiles: List<File>
        get() = collectionBookFolderPref
            .map(::File)
            .flatMap { it.listFilesSafely(FileRecognition.folderAndMusicFilter) }
            .sortedWith(NaturalOrderComparator.fileComparator)


    // check for new books
    private suspend fun checkForBooks() {
        val singleBooks = singleBookFiles
        for (f in singleBooks) {
            if (f.isFile && f.canRead()) {
                checkBook(f, BookType.BookSingleFile)
            } else if (f.isDirectory && f.canRead()) {
                checkBook(f, BookType.BookSingleFolder)
            }
        }

        val collectionBooks = collectionBookFiles
        for (f in collectionBooks) {
            if (f.isFile && f.canRead()) {
                checkBook(f, BookType.BookCollectionFile)
            } else if (f.isDirectory && f.canRead()) {
                checkBook(f, BookType.BookCollectionFolder)
            }
        }
    }

    private suspend fun checkBook(file: File, bookType: BookType) {
        val bookExisting = getBookFromDb(file, bookType)
        val bookId = bookExisting?.bid ?: UUID.randomUUID()
        val chapters = getChaptersByRootFile(file, bookId)

        if (bookExisting != null) {
            if (chapters.isEmpty()) {
                repository.deleteBook(bookId)
            }
        } else {
            if (chapters.isNotEmpty()) {
                addNewBook(bookId, chapters, file, bookType)
            }
        }
    }

    private suspend fun addNewBook(
        bookId: UUID,
        chapters: List<ChapterDataModel>,
        file: File,
        bookType: BookType,
    ) {
        val bookRoot = if (file.isDirectory) file.absolutePath else file.parent!!

        val firstChapterFile = chapters.first().file
        val result =
            mediaAnalyzer.analyze(firstChapterFile) as? MediaAnalyzer.Result.Success ?: return

        var bookName = result.bookName
        if (bookName.isNullOrEmpty()) {
            bookName = result.chapterName
            if (bookName.isNullOrEmpty()) {
                val withoutExtension = file.nameWithoutExtension
                bookName = if (withoutExtension.isEmpty()) {
                    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
                    file.name!!
                } else {
                    withoutExtension
                }
            }
        }

        val existingBook = getBookFromDb(file, bookType)
        if (existingBook == null) {
            val newBook = BookDomainModel(
                bid = bookId,
                metaData = BookMetaData(
                    type = bookType,
                    author = result.author,
                    name = bookName,
                    root = bookRoot,
                    id = bookId,
                    addedAtMillis = System.currentTimeMillis()
                ),
                content = BookContent(
                    settings = BookSettings(
                        currentFile = firstChapterFile,
                        positionInChapter = 0,
                        id = bookId,
                        active = true,
                        lastPlayedAtMillis = 0
                    ),
                    chapters = chapters.withBookId(bookId),
                    id = bookId
                )
            )
            repository.addBook(newBook)
        }
    }

    private suspend fun getBookFromDb(rootFile: File, type: BookType): BookDomainModel? {
        val books = repository.getAllBooks()
        if (rootFile.isDirectory) {
            return books.firstOrNull {
                rootFile.absolutePath == it.root && type == it.type
            }
        } else if (rootFile.isFile) {
            for (b in books) {
                if (rootFile.parentFile?.absolutePath == b.root && type === b.type) {
                    val singleChapter = b.content.chapters.first()
                    if (singleChapter.file == rootFile) {
                        return b
                    }
                }
            }
        }
        return null
    }

    private suspend fun getChaptersByRootFile(
        rootFile: File,
        bookId: UUID,
    ): List<ChapterDataModel> {
        val files = rootFile.walk()
            .filter { FileRecognition.musicFilter.accept(it) }
            .sortedWith(NaturalOrderComparator.fileComparator)
            .toList()

        val media = ArrayList<ChapterDataModel>(files.size)

        for (f in files) {
            val chapter = repository.getChapterByRootFile(rootFile)
            if (chapter != null) {
                media.add(chapter)
                continue
            } else {
                val result = mediaAnalyzer.analyze(f)
                Timber.i("analyzed $f.")
                if (result is MediaAnalyzer.Result.Success) {
                    media.add(ChapterDataModel(
                        bookId = bookId,
                        file = f,
                        markData = result.chapters,
                        name = result.chapterName,
                        author = result.author ?: "Unknown",
                        duration = result.duration
                    ))
                }
            }
        }

        return media
    }

    private fun Iterable<ChapterDataModel>.withBookId(bookId: UUID): List<ChapterDataModel> {
        return map { chapter ->
            if (chapter.bookId == bookId) {
                chapter
            } else {
                chapter.copy(bookId = bookId)
            }
        }
    }

}

