package com.nikolam.feature_folders.folders_overview.presenter

import com.nikolam.common.navigation.NavManager
import com.nikolam.feature_folders.FolderManager
import com.nikolam.feature_folders.folder_chooser.presenter.OperationMode
import com.nikolam.common.viewmodel.BaseAction
import com.nikolam.common.viewmodel.BaseViewState
import timber.log.Timber

internal class FoldersOverviewViewModel (private val folderManager: FolderManager, private  val navManager: NavManager)
    : com.nikolam.common.viewmodel.BaseViewModel<FoldersOverviewViewModel.ViewState, FoldersOverviewViewModel.Action>(ViewState()){

    private lateinit var singleBookFolder: HashSet<String>
    private lateinit var collectionBookFolder: HashSet<String>
    
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