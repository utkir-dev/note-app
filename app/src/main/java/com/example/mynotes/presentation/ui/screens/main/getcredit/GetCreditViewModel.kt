package com.example.mynotes.presentation.ui.screens.main.getcredit

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow


interface GetCreditViewModel {
    val person: MutableState<PersonDomain>
    val persons: Flow<List<PersonDomain>>
    val currency: MutableState<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>
    val pocket: MutableState<PocketDomain>
    val pockets: Flow<List<PocketDomain>>

    fun setPerson(person: PersonDomain)
    fun setCurrency(currency: CurrencyDomain)
    fun setPocket(pocket: PocketDomain)
    fun back()
    val balances: Flow<List<BalanceDomain>>
    fun addTransaction(amountTransaction: Double, comment: String, balance: Double)

}