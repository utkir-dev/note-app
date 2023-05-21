package com.example.mynotes.presentation.ui.screens.main.balance

import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface BalanceViewModel {
    val pockets: Flow<List<PocketDomain>>
    val wallets: Flow<List<WalletDomain>>
    val currencies: Flow<List<CurrencyDomain>>
    fun back()
}