package com.example.mynotes.presentation.ui.screens.main.convertation

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface ConvertationViewModel {
    val pocketFrom: MutableState<PocketDomain>
    val pocketTo: MutableState<PocketDomain>

    val currencyFrom: MutableState<CurrencyDomain>
    val currencyTo: MutableState<CurrencyDomain>
    val currency: MutableState<CurrencyDomain>

    val pockets: Flow<List<PocketDomain>>
    val wallets: Flow<List<WalletDomain>>
    val currencies: Flow<List<CurrencyDomain>>

    fun setCurrencyFrom(currency: CurrencyDomain)
    fun setCurrencyTo(currency: CurrencyDomain)
    fun setCurrency(currency: CurrencyDomain)

    fun setPocketFrom(pocket: PocketDomain)
    fun setPocketTo(pocket: PocketDomain)

    fun back()
    val balances: Flow<List<BalanceDomain>>
}