package com.example.mynotes.presentation.ui.screens.home

import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import kotlinx.coroutines.flow.Flow

interface HomeViewModel {
    fun onEventDispatcher(type: DirectionType)
    fun checkNotUploads()
    val balances: Flow<List<BalanceDomain>>
    val currencies: Flow<List<CurrencyDomain>>
}