package com.example.mynotes.presentation.ui.screens.main.balance

import com.example.mynotes.presentation.ui.directions.common.DirectionType

interface BalanceViewModel {
    fun onEventDispatcher(type: DirectionType)
}