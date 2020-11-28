package com.nikolam.book_overview.folders_overview.presenter

import com.nikolam.book_overview.FolderManager
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.common.viewmodel.BaseAction
import com.nikolam.common.viewmodel.BaseViewState
import timber.log.Timber

internal class FoldersOverviewViewModel (private val folderManager: FolderManager, private  val navManager: com.nikolam.common.NavManager)
    : com.nikolam.common.viewmodel.BaseViewModel<FoldersOverviewViewModel.ViewState, FoldersOverviewViewModel.Action>(ViewState()){

    private lateinit var singleBookFolder: HashSet<String>
    private lateinit var collectionBookFolder: HashSet<String>

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
        singleBookFolder = HashSet(folderManager.provideBookSingleFolders())
        collectionBookFolder = HashSet(folderManager.provideBookCollectionFolders())

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

    fun deleteFolder(folder : String){
        folderManager.removeFolder(folder)
        singleBookFolder.remove(folder)
        collectionBookFolder.remove(folder)
        val combinedFolders = singleBookFolder.plus(collectionBookFolder)

        sendAction(Action.FilesLoadingSuccess( combinedFolders.toList()))
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