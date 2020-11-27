package com.nikolam.book_overview.folder_chooser.presenter

import android.annotation.SuppressLint
import com.nikolam.book_overview.BookManager
import com.nikolam.book_overview.folder_chooser.data.StorageDirFinder
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
    private val bookManager: BookManager,
private val navManager: com.nikolam.common.NavManager
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
            currentFolderName = viewAction.name
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
        collectionBookFolder = bookManager.provideBookCollectionFolders()
        singleBookFolder = bookManager.provideBookSingleFolders()

        Timber.d("SingleBooks chooser %s", singleBookFolder.toString())
        Timber.d("Collections chooser %s", collectionBookFolder.toString())

        refreshRootDirs()
    }

    fun setOperationMode(om: OperationMode) {
        operationMode = om
        Timber.d("Operation mode is %s", operationMode.toString())
    }

    @SuppressLint("MissingPermission")
    private fun refreshRootDirs() {
        rootDirs.clear()
        rootDirs.addAll(storageDirFinder.storageDirs())
        //setChooseButtonEnabled(rootDirs.isNotEmpty())

        when {
            chosenFile != null -> fileSelected(chosenFile)
            rootDirs.isNotEmpty() -> fileSelected(rootDirs.first())
            else -> fileSelected(null)
        }
    }

    fun fileChosen() {
        addFileAndTerminate(chosenFile!!)
    }

    private fun addFileAndTerminate(chosen: File) {
        if (canAddNewFolder(chosen.absolutePath)) {
            bookManager.saveSelectedFolder(chosen.absolutePath, operationMode)
        }
        navManager.navigate(FolderChooserFragmentDirections.actionFolderChooserFragmentToFoldersOverviewFragment())
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

    fun fileSelected(selectedFile: File?) {
        chosenFile = selectedFile
        showNewData(selectedFile?.closestFolder()?.getContentsSorted() ?: emptyList(),
            selectedFile?.name ?: "")
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
        val currentFolderName: String = ""
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        class FilesLoadingSuccess(val files: List<File>, val name: String) : Action()
        object FilesLoadingFailure : Action()
    }
}

/**
 * Returns the closest folder. If this is a folder return itself. Else return the parent.
 */
private fun File.closestFolder(): File = if (isDirectory) {
    this
} else {
    parentFile!!
}

/** Gets the containing files of a folder (restricted to music and folders) in a naturally sorted order.  */
private fun File.getContentsSorted() = listFilesSafely(com.nikolam.common.FileRecognition.folderAndMusicFilter)
    .sortedWith(com.nikolam.common.NaturalOrderComparator.fileComparator)

/**
 * As there are cases where [File.listFiles] returns null even though it is a directory, we return
 * an empty list instead.
 */
fun File.listFilesSafely(filter: FileFilter? = null): List<File> {
    val array: Array<File>? = if (filter == null) listFiles() else listFiles(filter)
    return array?.toList() ?: emptyList()
}
