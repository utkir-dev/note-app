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
        navigator.navigateTo(appScreens.incomeScreen())
    }

    override suspend fun navigateToOutcome() {
        navigator.navigateTo(appScreens.outcomePocketScreen())
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
        navigator.navigateTo(appScreens.pocketScreen())
    }

    override suspend fun navigateToCurrencies() {
        navigator.navigateTo(appScreens.currencyScreen())
    }

    override suspend fun navigateToHistory() {
        navigator.navigateTo(appScreens.historyScreen())
    }

    override suspend fun navigateToSignIn() {
        navigator.replaceAll(appScreens.signInScreen())
    }
}