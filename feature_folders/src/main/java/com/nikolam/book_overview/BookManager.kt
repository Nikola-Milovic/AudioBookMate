package com.nikolam.book_overview

import android.content.SharedPreferences
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.book_overview.misc.BOOK_COLLECTION
import com.nikolam.book_overview.misc.BOOK_SINGLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class BookManager(private val sharedPreferences: SharedPreferences){


    private lateinit var bookSingles : MutableSet<String>
    private lateinit var bookCollections: MutableSet<String>

    init{
        GlobalScope.launch(Dispatchers.IO) {
            bookSingles = HashSet(sharedPreferences.getStringSet(BOOK_SINGLE, mutableSetOf()) ?: mutableSetOf())
        }
        GlobalScope.launch(Dispatchers.IO){
            bookCollections = HashSet(sharedPreferences.getStringSet(BOOK_COLLECTION, mutableSetOf()) ?:  mutableSetOf())
        }
    }

    fun saveSelectedFolder(file : String, operationMode: OperationMode) {
        when(operationMode){
            OperationMode.COLLECTION_BOOK -> {
                if(!bookSingles.contains(file)) bookCollections.add(file)
            }
            OperationMode.SINGLE_BOOK -> {
                if(!bookCollections.contains(file)) bookSingles.add(file)
            }
        }
        saveBookFoldersPreferences()
    }

    fun provideBookFolders() : Set<String>{
        return bookSingles.plus(bookCollections)
    }

    fun provideBookCollectionFolders() : Set<String>{
        return bookCollections
    }

    fun provideBookSingleFolders() : Set<String>{
        return bookSingles
    }

    private fun saveBookFoldersPreferences(){
        Timber.d("Saved to preferences")
        GlobalScope.launch(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putStringSet(BOOK_SINGLE, bookSingles)
                putStringSet(BOOK_COLLECTION, bookCollections)
                apply()
            }
        }
    }

}