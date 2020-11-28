package com.nikolam.feature_folders.di

import com.nikolam.feature_folders.folder_chooser.data.StorageDirFinder
import com.nikolam.feature_folders.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.feature_folders.folders_overview.presenter.FoldersOverviewViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val storageModule = module {
    single{StorageDirFinder(get())}
}

val viewModelModule : Module = module {
    viewModel{FolderChooserViewModel(get(), get(), get())}
    viewModel{FoldersOverviewViewModel(get(), get())}
}


