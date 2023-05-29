package com.example.mynotes.presentation.ui.screens.main.income

import androidx.compose.runtime.MutableState
import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow


interface IncomeViewModel {
    val pocket: MutableState<PocketDomain>
    val pockets: Flow<List<PocketDomain>>
    val currency: MutableState<CurrencyDomain>
    val currencies: Flow<List<CurrencyDomain>>
    val balances: Flow<List<BalanceDomain>>

    fun setPocket(pocket: PocketDomain)
    fun setCurrency(currency: CurrencyDomain)
    fun add(pocket: PocketDomain)
    fun back()
    fun addTransaction(amountTransaction: Double, comment: String, balance: Double)
}