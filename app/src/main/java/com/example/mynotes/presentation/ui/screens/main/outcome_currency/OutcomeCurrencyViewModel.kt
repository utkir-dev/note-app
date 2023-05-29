package com.example.mynotes.presentation.ui.screens.main.outcome_currency

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface OutcomeCurrencyViewModel {
    val pocket: MutableState<PocketDomain>
    val currency: MutableState<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>
    val wallet: MutableState<WalletDomain>
    val wallets: Flow<List<WalletDomain>>
    val balances: Flow<List<BalanceDomain>>
    fun setCurrency(currency: CurrencyDomain)
    fun setPocket(pocketId: String)
    fun back()
    fun setWallet(value: WalletDomain)

    fun addTransaction(amountTransaction: Double, comment: String, balance: Double)

}