package com.nikolam.book_overview

import android.content.SharedPreferences
import com.nikolam.book_overview.misc.BOOK_COLLECTION
import com.nikolam.book_overview.misc.BOOK_SINGLE
import timber.log.Timber

class BookManager(private val sharedPreferences: SharedPreferences){

    private var bookSingles : Set<String> = sharedPreferences.getStringSet(BOOK_SINGLE, emptySet()) ?: emptySet()
    private var bookCollections: Set<String>  = sharedPreferences.getStringSet(BOOK_COLLECTION, emptySet()) ?: emptySet()

    fun saveSelectedBookFolders(collections : Set<String>, singles : Set<String>) {
        saveBookCollectionsToPreferences(collections)
        saveBookSinglesToPreferences(singles)

        Timber.d("Save books %s, %s", bookSingles.toString(), bookCollections.toString())
    }

    fun provideBookCollectionFolders() : Set<String>{
        return sharedPreferences.getStringSet(BOOK_COLLECTION, emptySet()) ?: emptySet()
    }

    fun provideBookSingleFolders() : Set<String>{
        return sharedPreferences.getStringSet(BOOK_SINGLE, emptySet()) ?: emptySet()
    }

    private fun saveBookCollectionsToPreferences(books : Set<String>){
        if(books == bookCollections){
            return
        }

        bookCollections =  books

        with(sharedPreferences.edit()) {
            putStringSet(BOOK_COLLECTION, bookCollections)
            apply()
        }

    }
    private fun saveBookSinglesToPreferences(books : Set<String>){
        if(books == bookSingles){
            return
        }

        bookSingles = books

        with(sharedPreferences.edit()) {
            putStringSet(BOOK_SINGLE, bookCollections)
            apply()
        }
    }

}