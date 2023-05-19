package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyDirection
import com.example.mynotes.presentation.ui.screens.main.home.HomeDirection
import com.example.mynotes.presentation.ui.screens.main.pockets.PocketDirection
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