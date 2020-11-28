package com.nikolam.feature_folders

import android.content.SharedPreferences
import com.nikolam.feature_folders.folder_chooser.presenter.OperationMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class FolderManager(private val sharedPreferences: SharedPreferences){

    private lateinit var bookSingles : MutableSet<String>
    private lateinit var bookCollections: MutableSet<String>

    init{
        GlobalScope.launch(Dispatchers.IO) {
            bookSingles = HashSet(sharedPreferences.getStringSet(com.nikolam.common.BOOK_SINGLE, mutableSetOf()) ?: mutableSetOf())
        }
        GlobalScope.launch(Dispatchers.IO){
            bookCollections = HashSet(sharedPreferences.getStringSet(com.nikolam.common.BOOK_COLLECTION, mutableSetOf()) ?:  mutableSetOf())
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

    fun provideBookCollectionFolders() : Set<String>{
        return bookCollections
    }

    fun provideBookSingleFolders() : Set<String>{
        return bookSingles
    }

    fun removeFolder(f : String) {
        bookCollections.remove(f)
        bookSingles.remove(f)
        saveBookFoldersPreferences()
    }

    private fun saveBookFoldersPreferences(){
        Timber.d("Saved to preferences")
        GlobalScope.launch(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putStringSet(com.nikolam.common.BOOK_SINGLE, bookSingles)
                putStringSet(com.nikolam.common.BOOK_COLLECTION, bookCollections)
                apply()
            }
        }
    }

}