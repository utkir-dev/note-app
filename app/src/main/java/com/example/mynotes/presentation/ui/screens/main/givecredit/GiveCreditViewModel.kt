package com.example.mynotes.presentation.ui.screens.main.givecredit

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.*
import kotlinx.coroutines.flow.Flow


interface GiveCreditViewModel {
    val person: Flow<PersonDomain>
    val persons: Flow<List<PersonDomain>>
    val currency: Flow<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>
    val pocket: Flow<PocketDomain>
    val pockets: Flow<List<PocketDomain>>

    fun setPerson(person: PersonDomain)
    fun setCurrency(currency: CurrencyDomain)
    fun setPocket(pocket: PocketDomain)
    fun addTransaction(amountTransaction: Double, comment: String, balance: Double)
    fun back()
    val wallets: Flow<List<WalletDomain>>
    val balances: Flow<List<BalanceDomain>>
}