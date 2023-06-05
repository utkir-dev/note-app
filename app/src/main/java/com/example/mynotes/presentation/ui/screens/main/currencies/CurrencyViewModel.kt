package com.example.mynotes.presentation.ui.screens.main.currencies

import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface CurrencyViewModel {
    val currencies: Flow<List<CurrencyDomain>>
    fun add(currency: CurrencyDomain)
    fun update(currency: CurrencyDomain)
    fun delete()
    fun back()
    var wallets: Flow<List<WalletDomain>>
}