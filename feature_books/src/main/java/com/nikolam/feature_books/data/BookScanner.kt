package com.nikolam.feature_books.data

import android.content.SharedPreferences
import com.nikolam.common.BOOK_COLLECTION
import com.nikolam.common.BOOK_SINGLE
import com.nikolam.common.FileRecognition
import com.nikolam.common.extensions.listFilesSafely
import com.nikolam.feature_books.misc.BookTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class BookScanner(private val sharedPreferences: SharedPreferences){

    lateinit var singleBookFolderPref : Set<String>
    lateinit var collectionBookFolderPref : Set<String>

    init{
        GlobalScope.launch(Dispatchers.IO) {
            singleBookFolderPref = HashSet(sharedPreferences.getStringSet(BOOK_SINGLE, mutableSetOf()) ?: mutableSetOf())
        }
        GlobalScope.launch(Dispatchers.IO){
            collectionBookFolderPref = HashSet(sharedPreferences.getStringSet(BOOK_COLLECTION, mutableSetOf()) ?:  mutableSetOf())
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
                checkBook(f, BookTypes.BookSingleFile)
            } else if (f.isDirectory && f.canRead()) {
                checkBook(f, BookTypes.BookSingleFolder)
            }
        }

        val collectionBooks = collectionBookFiles
        for (f in collectionBooks) {
            if (f.isFile && f.canRead()) {
                checkBook(f, BookTypes.BookCollectionFile)
            } else if (f.isDirectory && f.canRead()) {
                checkBook(f, BookTypes.BookCollectionFolder)
            }
        }
    }

    private fun checkBook(file : File, bookType: BookTypes) {

    }
}