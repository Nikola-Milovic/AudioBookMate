package com.nikolam.feature_folders.folder_chooser.presenter

import android.annotation.SuppressLint
import com.nikolam.common.extensions.closestFolder
import com.nikolam.common.extensions.getContentsSorted
import com.nikolam.common.navigation.NavManager
import com.nikolam.feature_folders.FolderManager
import com.nikolam.feature_folders.folder_chooser.data.StorageDirFinder
import com.nikolam.common.viewmodel.BaseAction
import com.nikolam.common.viewmodel.BaseViewState
import timber.log.Timber
import java.io.File
import java.io.FileFilter
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.min

enum class OperationMode {
    COLLECTION_BOOK,
    SINGLE_BOOK
}

internal class FolderChooserViewModel(
    private val storageDirFinder: StorageDirFinder,
    private val folderManager: FolderManager,
    private val navManager: NavManager
) : com.nikolam.common.viewmodel.BaseViewModel<FolderChooserViewModel.ViewState, FolderChooserViewModel.Action>(ViewState()) {

    private val rootDirs = ArrayList<File>()
    private var chosenFile: File? = null
    private lateinit var operationMode: OperationMode

    private lateinit var singleBookFolder: Set<String>
    private lateinit var collectionBookFolder: Set<String>

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.FilesLoadingSuccess -> state.copy(
            isLoading = false,
            isError = false,
            files = viewAction.files,
            currentFolderName = viewAction.name,
            upButtonEnabled = canGoBack()
        )
        is Action.FilesLoadingFailure -> state.copy(
            isLoading = false,
            isError = true,
            files = listOf(),
            currentFolderName = "Error"
        )
    }

    override fun onLoadData() {
        super.onLoadData()

        collectionBookFolder = folderManager.provideBookCollectionFolders()
        singleBookFolder = folderManager.provideBookSingleFolders()

        refreshRootDirs()
    }

    fun setOperationMode(om: OperationMode) {
        operationMode = om
        Timber.d("Operation mode is %s", operationMode.toString())
    }

    //TODO: pop the backstack
    fun goBackToPreviousScreen(){
        navManager.navigate(FolderChooserFragmentDirections.actionFolderChooserFragmentToFoldersOverviewFragment())
    }

    fun fileChosen() {
        addFileAndTerminate(chosenFile!!)
    }

    fun fileSelected(selectedFile: File?) {
        chosenFile = selectedFile
        showNewData(selectedFile?.closestFolder()?.getContentsSorted() ?: emptyList(),
            selectedFile?.toString() ?: "")
    }

    @SuppressLint("MissingPermission")
    private fun refreshRootDirs() {
        rootDirs.clear()
        rootDirs.addAll(storageDirFinder.storageDirs())

        when {
            chosenFile != null -> fileSelected(chosenFile)
            rootDirs.isNotEmpty() -> fileSelected(rootDirs.first())
            else -> fileSelected(null)
        }
    }

    private fun addFileAndTerminate(chosen: File) {
        if (canAddNewFolder(chosen.absolutePath)) {
            folderManager.saveSelectedFolder(chosen.absolutePath, operationMode)
            goBackToPreviousScreen()
        }
    }

    private fun canGoBack(): Boolean {
        if (rootDirs.isEmpty()) {
            return false
        }

        // to go up we must not already be in top level
        return rootDirs.none { it == chosenFile!!.closestFolder() }
    }

    fun backConsumed(): Boolean {
        // Timber.d("up called. currentFolder=$chosenFile")
        return if (canGoBack()) {
            fileSelected(chosenFile!!.closestFolder().parentFile)
            true
        } else {
            false
        }
    }

    private fun showNewData(newData: List<File>, name: String) {
        sendAction(Action.FilesLoadingSuccess(newData, name))
    }

    private fun canAddNewFolder(newFile: String): Boolean {
        //Timber.v("canAddNewFolder called with $newFile")
        val folders = HashSet(collectionBookFolder)
        folders.addAll(singleBookFolder)

        // if this is the first folder adding is always allowed
        if (folders.isEmpty()) {
            return true
        }

        val newParts = newFile.split(File.separator)
        for (s in folders) {

            if (newFile == s) {
                Timber.i("file is already in the list.")
                // same folder, this should not be added
                return false
            }

            val oldParts = s.split(File.separator)
            val max = min(oldParts.size, newParts.size) - 1
            val filesAreSubsets = (0..max).none { oldParts[it] != newParts[it] }
            if (filesAreSubsets) {
                Timber.i("the files are sub folders of each other.")
                //  view.showSubFolderWarning(s, newFile)
                return false
            }
        }

        return true
    }

    internal data
    class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val files: List<File> = listOf(),
        val currentFolderName: String = "",
        val upButtonEnabled : Boolean = false
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        class FilesLoadingSuccess(val files: List<File>, val name: String) : Action()
        object FilesLoadingFailure : Action()
    }
}
