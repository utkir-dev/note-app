package com.example.mynotes.presentation.ui.screens.main.givecredit

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import kotlinx.coroutines.flow.Flow


interface GiveCreditViewModel {
    val person: MutableState<PersonDomain>
    val persons: Flow<List<PersonDomain>>
    val currency: MutableState<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>
    val pocket: MutableState<PocketDomain>
    val pockets: Flow<List<PocketDomain>>

    fun setPerson(person: PersonDomain)
    fun setCurrency(currency: CurrencyDomain)
    fun setPocket(pocket: PocketDomain)
    fun addTransaction(amountTransaction: Double, comment: String)
    fun back()
    val wallets: Flow<List<WalletDomain>>
}