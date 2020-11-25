package com.nikolam.book_overview.folders_overview.presenter

import com.igorwojda.showcase.library.base.presentation.viewmodel.BaseAction
import com.igorwojda.showcase.library.base.presentation.viewmodel.BaseViewState
import com.nikolam.book_overview.BookManager
import com.nikolam.book_overview.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.book_overview.misc.viewmodel.BaseViewModel
import timber.log.Timber
import java.io.File

internal class FoldersOverviewViewModel (private val bookManager: BookManager)
    : BaseViewModel<FoldersOverviewViewModel.ViewState, FoldersOverviewViewModel.Action>(ViewState()){

    private var singleBookFolder: Set<String>
    private var collectionBookFolder: Set<String>

    override fun onReduceState(viewAction: Action) = when(viewAction) {
        is Action.FilesLoadingSuccess -> state.copy(
            isLoading = false,
            isError  = false,
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

    init {
        singleBookFolder = bookManager.provideBookSingleFolders()
        collectionBookFolder = bookManager.provideBookCollectionFolders()

        Timber.d("SingleBooks %s", singleBookFolder.toString())
        Timber.d("Collections %s", collectionBookFolder.toString())
    }

    override fun onLoadData() {
        super.onLoadData()
    }

    internal data
    class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val files : List<File> = listOf(),
        val currentFolderName : String = ""
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        class FilesLoadingSuccess(val files : List<File>, val name : String) : Action()
        object FilesLoadingFailure : Action()
    }
}