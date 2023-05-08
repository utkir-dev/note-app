package com.example.mynotes.presentation.ui.directions

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.home.HomeDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class HomeDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
    private val appScreens: AppScreens
) : HomeDirection {
    override suspend fun navigateToBalance() {
        navigator.navigateTo(appScreens.balanceScreen())
    }

    override suspend fun navigateToIncome() {

    }

    override suspend fun navigateToOutIncome() {

    }

    override suspend fun navigateToGetCredit() {

    }

    override suspend fun navigateToGiveCredit() {

    }

    override suspend fun navigateToCreditors() {

    }

    override suspend fun navigateToDebetors() {

    }

    override suspend fun navigateToPockets() {

    }

    override suspend fun navigateToCurrencies() {
        navigator.navigateTo(appScreens.currencyScreen())
    }

    override suspend fun navigateToHistory() {

    }
}