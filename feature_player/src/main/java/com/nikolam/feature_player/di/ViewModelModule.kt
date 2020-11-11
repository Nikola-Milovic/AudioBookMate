package com.nikolam.feature_player.di

import com.nikolam.feature_player.presentation.PlayerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { PlayerViewModel() }
}
