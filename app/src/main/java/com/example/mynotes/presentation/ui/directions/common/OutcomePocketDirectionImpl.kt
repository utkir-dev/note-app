package com.example.mynotes.presentation.ui.directions.common

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.outcome_pocket.OutcomePocketDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class OutcomePocketDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : OutcomePocketDirection {
    override suspend fun back() {
        navigator.back()
    }

    override suspend fun navigateToCurrency(pocketId: String) {
        navigator.navigateTo(appScreens.outcomeCurrencyScreen(pocketId))
    }
}