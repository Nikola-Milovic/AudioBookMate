package com.nikolam.book_overview

import android.content.SharedPreferences
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.book_overview.misc.BOOK_COLLECTION
import com.nikolam.book_overview.misc.BOOK_SINGLE

class BookManager(private val sharedPreferences: SharedPreferences){


    private var bookSingles : MutableSet<String> = sharedPreferences.getStringSet(BOOK_SINGLE, emptySet()) ?: mutableSetOf()
    private var bookCollections: MutableSet<String>  = sharedPreferences.getStringSet(BOOK_COLLECTION, emptySet()) ?:  mutableSetOf()



    fun saveSelectedFolder(file : String, operationMode: OperationMode) {
        when(operationMode){
            OperationMode.COLLECTION_BOOK -> bookCollections.add(file)
            OperationMode.SINGLE_BOOK -> bookSingles.add(file)
        }
        saveBookFoldersPreferences()
    }

    fun provideBookFolders() : Set<String>{
        return bookSingles.plus(bookCollections)
    }

    fun provideBookCollectionFolders() : Set<String>{
        return sharedPreferences.getStringSet(BOOK_COLLECTION, emptySet()) ?: emptySet()
    }

    fun provideBookSingleFolders() : Set<String>{
        return sharedPreferences.getStringSet(BOOK_SINGLE, emptySet()) ?: emptySet()
    }

    private fun saveBookFoldersPreferences(){
        with(sharedPreferences.edit()) {
            putStringSet(BOOK_SINGLE, bookSingles)
            apply()
        }

        with(sharedPreferences.edit()) {
            putStringSet(BOOK_COLLECTION, bookCollections)
            apply()
        }
    }

}