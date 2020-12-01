package com.nikolam.feature_books.data

import android.content.SharedPreferences
import com.nikolam.common.BOOK_COLLECTION
import com.nikolam.common.BOOK_SINGLE
import com.nikolam.common.FileRecognition
import com.nikolam.common.NaturalOrderComparator
import com.nikolam.common.extensions.listFilesSafely
import com.nikolam.common.media.MediaAnalyzer
import com.nikolam.feature_books.domain.BookRepository
import com.nikolam.feature_books.domain.model.BookDomainModel
import com.nikolam.feature_books.domain.model.ChapterDomainModel
import com.nikolam.feature_books.misc.BookType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class BookScanner(
    private val sharedPreferences: SharedPreferences,
    private val repository: BookRepository
) {

    lateinit var singleBookFolderPref: Set<String>
    lateinit var collectionBookFolderPref: Set<String>

    lateinit var booksList: ArrayList<BookDomainModel>

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
            .sortedWith(com.nikolam.common.NaturalOrderComparator.fileComparator)

    private val collectionBookFiles: List<File>
        get() = collectionBookFolderPref
            .map(::File)
            .flatMap { it.listFilesSafely(FileRecognition.folderAndMusicFilter) }
            .sortedWith(com.nikolam.common.NaturalOrderComparator.fileComparator)


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

    private fun checkBook(file: File, bookType: BookType) {
        val exists = getBookFromDb(file, bookType)
        val chapters = getChaptersByRootFile(file)
    }

    private fun getBookFromDb(rootFile: File, type: BookType): BookDomainModel? {
        val books = repository.getAllBooks()
        if (rootFile.isDirectory) {
            return books.firstOrNull {
                rootFile.absolutePath == it.root && type == it.type
            }
        } else if (rootFile.isFile) {
            for (b in books) {
                if (rootFile.parentFile?.absolutePath == b.root && type === b.type) {
                    val singleChapter = b.content.first()
                    if (singleChapter.file == rootFile) {
                        return b
                    }
                }
            }
        }
        return null
    }

    private fun getChaptersByRootFile(rootFile: File): List<ChapterDomainModel> {
        val files = rootFile.walk()
            .filter { FileRecognition.musicFilter.accept(it) }
            .sortedWith(NaturalOrderComparator.fileComparator)
            .toList()

        val media = ArrayList<ChapterDomainModel>(files.size)

        for (f in files) {
            val chapter = repository.getChapterByRootFile(rootFile)
            if (chapter != null) {
                media.add(chapter)
                continue
            } else {
//                val result = mediaAnalyzer.analyze(f)
//                Timber.i("analyzed $f.")
//                if (result is MediaAnalyzer.Result.Success) {
//                   media.add(ChapterDomainModel( f))
//                }
            }
        }

        return media
    }
}

