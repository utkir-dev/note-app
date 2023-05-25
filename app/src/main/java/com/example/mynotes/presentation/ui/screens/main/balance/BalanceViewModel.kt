package com.example.mynotes.presentation.ui.screens.main.balance

import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface BalanceViewModel {
    val balances: Flow<List<BalanceDomain>>
    fun back()
}