package com.example.mynotes.presentation.ui.screens.main.home

import com.example.mynotes.domain.models.BalanceDomain
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import kotlinx.coroutines.flow.Flow

interface HomeViewModel {
    fun onEventDispatcher(type: DirectionType)
    val balances: Flow<List<BalanceDomain>>
}