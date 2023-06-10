package com.example.mynotes.presentation.ui.directions

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.main.balance.BalanceDirection
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyDirection
import com.example.mynotes.presentation.ui.screens.home.HomeDirection
import com.example.mynotes.presentation.ui.screens.main.income.IncomeDirection
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class BackDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : BackDirection {
    override suspend fun back() {
        navigator.back()
    }
}