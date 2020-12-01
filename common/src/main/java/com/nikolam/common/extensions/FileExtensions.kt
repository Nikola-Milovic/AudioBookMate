package com.nikolam.common.extensions

import java.io.File
import java.io.FileFilter

/**
 * Returns the closest folder. If this is a folder return itself. Else return the parent.
 */
fun File.closestFolder(): File = if (isDirectory) {
    this
} else {
    parentFile!!
}

/** Gets the containing files of a folder (restricted to music and folders) in a naturally sorted order.  */
fun File.getContentsSorted() = listFilesSafely(com.nikolam.common.FileRecognition.folderAndMusicFilter)
    .sortedWith(com.nikolam.common.NaturalOrderComparator.fileComparator)

/**
 * As there are cases where [File.listFiles] returns null even though it is a directory, we return
 * an empty list instead.
 */
fun File.listFilesSafely(filter: FileFilter? = null): List<File> {
    val array: Array<File>? = if (filter == null) listFiles() else listFiles(filter)
    return array?.toList() ?: emptyList()
}