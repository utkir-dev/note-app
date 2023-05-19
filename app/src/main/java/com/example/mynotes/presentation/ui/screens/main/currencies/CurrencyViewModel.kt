package com.example.mynotes.presentation.ui.screens.main.currencies

import com.example.mynotes.domain.models.CurrencyDomain
import kotlinx.coroutines.flow.Flow


interface CurrencyViewModel {
    val currencies: Flow<List<CurrencyDomain>>
    fun add(currency: CurrencyDomain)
    fun update(currency: CurrencyDomain)
    fun delete(currency: CurrencyDomain)
    fun back()
}