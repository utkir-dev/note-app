package com.example.mynotes.presentation.ui.screens.main.balance

import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface BalanceViewModel {
    val currencies: Flow<List<CurrencyDomain>>
    val wallets: Flow<List<WalletDomain>>
    fun back()
}