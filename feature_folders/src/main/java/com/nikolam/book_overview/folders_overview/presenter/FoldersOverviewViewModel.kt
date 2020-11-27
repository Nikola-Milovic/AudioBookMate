package com.nikolam.book_overview.folders_overview.presenter

import com.igorwojda.showcase.library.base.presentation.viewmodel.BaseAction
import com.igorwojda.showcase.library.base.presentation.viewmodel.BaseViewState
import com.nikolam.book_overview.BookManager
import com.nikolam.book_overview.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.book_overview.misc.NavManager
import com.nikolam.book_overview.misc.viewmodel.BaseViewModel
import timber.log.Timber
import java.io.File

internal class FoldersOverviewViewModel (private val bookManager: BookManager, private  val navManager: NavManager)
    : BaseViewModel<FoldersOverviewViewModel.ViewState, FoldersOverviewViewModel.Action>(ViewState()){

    private lateinit var singleBookFolder: Set<String>
    private lateinit var collectionBookFolder: Set<String>

    override fun onReduceState(viewAction: Action) = when(viewAction) {
        is Action.FilesLoadingSuccess -> state.copy(
            isLoading = false,
            isError  = false,
            files = viewAction.files
        )
        is Action.FilesLoadingFailure -> state.copy(
            isLoading = false,
            isError = true,
            files = listOf()
        )
    }

    override fun onLoadData() {
        super.onLoadData()
        singleBookFolder = bookManager.provideBookSingleFolders()
        collectionBookFolder = bookManager.provideBookCollectionFolders()

        val combinedFolders = singleBookFolder.plus(collectionBookFolder)

        Timber.d("SingleBooks overview %s", singleBookFolder.toString())
        Timber.d("Collections overview %s", collectionBookFolder.toString())
        Timber.d("Combined %s", combinedFolders.toString())

        sendAction(Action.FilesLoadingSuccess( combinedFolders.toList()))
    }

    fun startFolderChooserWithOperationMode(operationMode: OperationMode){
        val navDirections = FoldersOverviewFragmentDirections.actionFoldersOverviewFragmentToFolderChooserFragment(operationMode.toString())
        navManager.navigate(navDirections)
    }


    internal data
    class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val files : List<String> = listOf()
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        class FilesLoadingSuccess(val files : List<String>) : Action()
        object FilesLoadingFailure : Action()
    }
}