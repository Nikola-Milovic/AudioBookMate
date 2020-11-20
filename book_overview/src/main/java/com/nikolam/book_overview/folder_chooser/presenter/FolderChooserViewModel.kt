package com.nikolam.book_overview.folder_chooser.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.nikolam.book_overview.misc.NaturalOrderComparator
import com.nikolam.book_overview.folder_chooser.data.StorageDirFinder
import com.nikolam.book_overview.misc.FileRecognition
import java.io.File
import java.io.FileFilter
import java.util.*

class FolderChooserViewModel(private val storageDirFinder: StorageDirFinder) : ViewModel() {

    private val rootDirs = ArrayList<File>()
    private var chosenFile: File? = null

    @SuppressLint("MissingPermission")
    private fun refreshRootDirs() {
        rootDirs.clear()
        rootDirs.addAll(storageDirFinder.storageDirs())
        newRootFolders(rootDirs)
        //setChooseButtonEnabled(rootDirs.isNotEmpty())

        when {
            chosenFile != null -> fileSelected(chosenFile)
            rootDirs.isNotEmpty() -> fileSelected(rootDirs.first())
            else -> fileSelected(null)
        }
    }

    private fun newRootFolders(rootDirs: ArrayList<File>) {}

    private fun fileSelected(selectedFile: File?) {
        chosenFile = selectedFile

        showNewData(selectedFile?.closestFolder()?.getContentsSorted() ?: emptyList())
        setCurrentFolderText(selectedFile?.name ?: "")

    }

    private fun showNewData(newData: List<File>){}

    private fun setCurrentFolderText(newName: String){}

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
private fun File.getContentsSorted() = listFilesSafely(FileRecognition.folderAndMusicFilter)
    .sortedWith(NaturalOrderComparator.fileComparator)

/**
 * As there are cases where [File.listFiles] returns null even though it is a directory, we return
 * an empty list instead.
 */
fun File.listFilesSafely(filter: FileFilter? = null): List<File> {
    val array: Array<File>? = if (filter == null) listFiles() else listFiles(filter)
    return array?.toList() ?: emptyList()
}
