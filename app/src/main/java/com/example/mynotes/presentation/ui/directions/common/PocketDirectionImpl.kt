package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.main.pocket_info.PocketDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PocketDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PocketDirection {
    override suspend fun back() {
        navigator.back()
    }

}