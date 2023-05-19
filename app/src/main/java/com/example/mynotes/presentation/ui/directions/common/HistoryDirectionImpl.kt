package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyDirection
import com.example.mynotes.presentation.ui.screens.main.history.HistoryDirection
import com.example.mynotes.presentation.ui.screens.main.home.HomeDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class HistoryDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : HistoryDirection {
    override suspend fun back() {
        navigator.back()
    }
}