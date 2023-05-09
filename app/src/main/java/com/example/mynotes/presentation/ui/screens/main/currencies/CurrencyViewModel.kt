package com.example.mynotes.presentation.ui.screens.main.currencies

import com.example.mynotes.domain.models.CurrencyDomain


interface CurrencyViewModel {
    fun add(currency: CurrencyDomain)
    fun update()
    fun delete(currency: CurrencyDomain)
}